package co.grtk.um.service;


import co.grtk.um.dto.SecurityUser;
import co.grtk.um.exception.UserNotFoundException;
import co.grtk.um.model.PrincipalStatus;
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
                .findByEmailAndStatus(email, PrincipalStatus.REGISTERED)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
    }

    public UmUser loadUserByEmail(String email) throws UserNotFoundException {
        return umUserRepository
                .findByEmailAndStatus(email, PrincipalStatus.REGISTERED)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
    }

    public Iterable<UmUser> loadAllUsers(){
        return umUserRepository.findAll();
    }

    public UmUser saveUser(UmUser umUser) {
        return umUserRepository.save(umUser);
    }

}
