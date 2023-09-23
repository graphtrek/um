package co.grtk.um.service;


import co.grtk.um.dto.UserDTO;
import co.grtk.um.exception.UmException;
import co.grtk.um.exception.UserNotFoundException;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.UmUserPrivilege;
import co.grtk.um.model.UmUserRole;
import co.grtk.um.model.UmUserStatus;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.repository.UmUserRoleRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UmUserRepository umUserRepository;
    private final UmUserRoleRepository umUserRoleRepository;
    public final ModelMapper modelMapper;

    private static final String ERRORLOG = "User not found email: ";
    public UmUserDetailsService(UmUserRepository umUserRepository,
                                UmUserRoleRepository umUserRoleRepository,
                                ModelMapper modelMapper) {
        this.umUserRepository = umUserRepository;
        this.umUserRoleRepository = umUserRoleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return umUserRepository
                .findByEmailAndStatus(email, UmUserStatus.REGISTERED)
                .map(umUser ->
                        new org.springframework.security.core.userdetails.User(
                                umUser.getEmail(),
                                umUser.getPassword(),
                                umUser.isEnabled(),
                                true,
                                true,
                                true,
                                getAuthorities(umUser.getRoles())))
                .orElseThrow(() -> new UsernameNotFoundException(ERRORLOG + email));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<UmUserRole> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<UmUserRole> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<UmUserPrivilege> collection = new ArrayList<>();
        for (final UmUserRole role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getUmUserPrivileges());
        }
        for (final UmUserPrivilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }


    public UmUser findByEmail(String email) throws UserNotFoundException {
        return umUserRepository
                .findByEmailAndStatus(email, UmUserStatus.REGISTERED)
                .orElseThrow(() -> new UsernameNotFoundException(ERRORLOG + email));
    }

    @Transactional
    public UserDTO loadUser(String email) throws UserNotFoundException {
        UmUser umUser = findByEmail(email);
        return modelMapper.map(umUser,UserDTO.class);
    }

    @Transactional
    public List<UserDTO> loadAllUsers(){
        return Arrays.stream(modelMapper.map(umUserRepository.findAll(),UserDTO[].class)).toList();
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        UmUser umUser = umUserRepository
                .findByIdAndEmail(userDTO.getId(),userDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        ERRORLOG + userDTO.getEmail()));
        umUser.setName(userDTO.getName());
        mapUmUserDTO(umUser,userDTO);
        umUser = umUserRepository.save(umUser);
        return modelMapper.map(umUser,UserDTO.class);
    }

    @Transactional
    public UserDTO saveProfile(String email, UserDTO userDTO) {
        UmUser umUser = umUserRepository
                    .findByIdAndEmailAndStatus(userDTO.getId(),email, UmUserStatus.REGISTERED)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "UserProfile not found id:" + userDTO.getId() + " email: " + email));

        mapUmUserDTO(umUser,userDTO);
        umUser = umUserRepository.save(umUser);
        return modelMapper.map(umUser,UserDTO.class);
    }

    private void mapUmUserDTO(UmUser umUser, UserDTO userDTO) {
        umUser.setStatus(userDTO.getStatus());
        Set<UmUserRole> roles = Arrays.stream(userDTO.getRoles().split(","))
                .map( role -> umUserRoleRepository.findByName(role).orElseThrow(
                        () -> new UmException("Inconsistenet Database UmUserRole:" + role + " does not exists")
                )).collect(Collectors.toSet());

        umUser.setRoles(roles);
    }


}
