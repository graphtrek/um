package co.grtk.um.service;

import co.grtk.um.dto.UserDTO;
import co.grtk.um.exception.UmException;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.UmUserRole;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.repository.UmUserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UmUserService {
    public final ModelMapper modelMapper;
    public final UmUserRepository umUserRepository;
    public final UmUserRoleRepository umUserRoleRepository;

    private static final String ERRORLOG = "User not found email: ";

    @Transactional
    public List<UserDTO> loadAllUsers(){
        List<UserDTO> users = new ArrayList<>();
        umUserRepository.findAll().forEach(umUser -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(umUser.getId());
            userDTO.setStatus(umUser.getStatus());
            userDTO.setName(umUser.getName());
            userDTO.setEmail(umUser.getEmail());
            userDTO.setPhone(umUser.getPhone());
            userDTO.setMfaType(umUser.getMfaType());
            userDTO.setRoles(
                    umUser.getRoles().stream()
                            .map(UmUserRole::getName)
                            .collect(Collectors.joining(",")));
            users.add(userDTO);
        });
        return users;
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        UmUser umUser = umUserRepository
                .findByIdAndEmail(userDTO.getId(),userDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        ERRORLOG + userDTO.getEmail()));
        mapUmUserDTO(umUser,userDTO);
        umUser = umUserRepository.save(umUser);
        return modelMapper.map(umUser,UserDTO.class);
    }

    private void mapUmUserDTO(UmUser umUser, UserDTO userDTO) {
        umUser.setName(userDTO.getName());
        umUser.setMfaType(userDTO.getMfaType());
        umUser.setPhone(userDTO.getPhone());
        umUser.setStatus(userDTO.getStatus());
        if(StringUtils.isNotBlank(userDTO.getRoles())) {
            Set<UmUserRole> roles = Arrays.stream(userDTO.getRoles().split(","))
                    .map(role -> umUserRoleRepository.findByName(role).orElseThrow(
                            () -> new UmException("Inconsistent Database UmUserRole:" + role + " does not exists")
                    )).collect(Collectors.toSet());
            umUser.setRoles(roles);
        }
    }

}
