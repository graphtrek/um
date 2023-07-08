package co.grtk.um.service;


import co.grtk.um.dto.SecurityUser;
import co.grtk.um.model.PrincipalStatus;
import co.grtk.um.repository.PrincipalRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UmUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final PrincipalRepository principalRepository;

    public UmUserDetailsService(PrincipalRepository principalRepository) {
        this.principalRepository = principalRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return principalRepository
                .findByEmailAndStatus(email, PrincipalStatus.REGISTERED)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
    }

}
