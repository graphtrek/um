package co.grtk.um.service;


import co.grtk.um.model.UserStatus;
import co.grtk.um.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import co.grtk.um.dto.SecurityUser;

@Service
public class UmUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UmUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmailAndUserStatus(email, UserStatus.REGISTERED)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
    }
}
