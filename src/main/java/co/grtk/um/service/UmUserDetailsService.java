package co.grtk.um.service;


import co.grtk.um.dto.SecurityUser;
import co.grtk.um.dto.UserDTO;
import co.grtk.um.exception.UserNotFoundException;
import co.grtk.um.model.UmUserStatus;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.UmUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UmUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UmUserRepository umUserRepository;

    public UmUserDetailsService(UmUserRepository umUserRepository) {
        this.umUserRepository = umUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return umUserRepository
                .findByEmailAndStatus(email, UmUserStatus.REGISTERED)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
    }

    public UmUser loadUserByEmail(String email) throws UserNotFoundException {
        return umUserRepository
                .findByEmailAndStatus(email, UmUserStatus.REGISTERED)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
    }


    public Iterable<UmUser> loadAllUsers(){
        return umUserRepository.findAll();
    }

    public UmUser saveUser(UserDTO userDTO) {
        UmUser umUser = umUserRepository
                .findByIdAndEmail(userDTO.getId(),userDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found email: " + userDTO.getEmail()));
        umUser.setStatus(userDTO.getStatus());
        umUser.setName(userDTO.getName());
        umUser.setRoles(userDTO.getRoles());
        return umUserRepository.save(umUser);
    }

    public UmUser saveProfile(String email, UserDTO userDTO) {
        UmUser umUser = umUserRepository
                    .findByIdAndEmailAndStatus(userDTO.getId(),email, UmUserStatus.REGISTERED)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "UserProfile not found id:" + userDTO.getId() + " email: " + email));
        umUser.setName(userDTO.getName());
        umUser.setRoles(userDTO.getRoles());
        return umUserRepository.save(umUser);
    }

}
