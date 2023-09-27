package co.grtk.um.service;


import co.grtk.um.model.UmUserPrivilege;
import co.grtk.um.model.UmUserRole;
import co.grtk.um.model.UmUserStatus;
import co.grtk.um.repository.UmUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UmUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UmUserRepository umUserRepository;

    private static final String ERRORLOG = "User not found email: ";
    public UmUserDetailsService(UmUserRepository umUserRepository) {
        this.umUserRepository = umUserRepository;
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
}
