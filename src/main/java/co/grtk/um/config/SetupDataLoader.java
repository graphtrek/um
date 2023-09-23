package co.grtk.um.config;

import co.grtk.um.exception.UmException;
import co.grtk.um.model.*;
import co.grtk.um.repository.UmUserPrivilegeRepository;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.repository.UmUserRoleRepository;
import co.grtk.um.service.TotpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final UmUserRepository userRepository;
    private final UmUserRoleRepository roleRepository;
    private final UmUserPrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpService totpService;
    private final ApplicationConfig applicationConfig;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final UmUserPrivilege readPrivilege =
                createPrivilegeIfNotFound("READ_PRIVILEGE");
        final UmUserPrivilege writePrivilege =
                createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final UmUserPrivilege configPrivilege =
                createPrivilegeIfNotFound("CONFIG_PRIVILEGE");

        // == create initial roles
        final Set<UmUserPrivilege> adminPrivileges = Set.of(readPrivilege, writePrivilege, configPrivilege);
        final Set<UmUserPrivilege> userPrivileges = Set.of(readPrivilege, writePrivilege);
        final UmUserRole adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final UmUserRole userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);

        // == create initial user
        String secret = generateSecret();
        var adminUser = createUserIfNotFound(
                applicationConfig.getAdminUserName(),
                applicationConfig.getAdminUserEmail(),
                applicationConfig.getAdminUserPassword(),
                Set.of(adminRole),
                secret,
                MfaType.APP);
        log.info("Created ADMIN USER {}", adminUser);

        var testUser = createUserIfNotFound(
                applicationConfig.getTestUserName(),
                applicationConfig.getTestUserEmail(),
                applicationConfig.getTestUserPassword(),
                Set.of(userRole),
                "",
                MfaType.NONE);
        log.info("Created TEST USER {}", testUser);
        alreadySetup = true;
    }

    private String generateSecret() {
        String secret = totpService.generateSecret();
        log.info("Secret:{}",secret);
        try {
            String actualCode = totpService.generateCode(secret, 30);
            boolean isValid = totpService.isValidCode(secret, actualCode,30);
            log.info("Admin:{} secret:{} actualCode:{} isValid:{}",
                    applicationConfig.getAdminUserName(), secret, actualCode, isValid);
        } catch (Exception e) {
            throw new UmException("Unable to start application", e);
        }
        return secret;
    }

    @Transactional
    public UmUserPrivilege createPrivilegeIfNotFound(final String name) {
        return privilegeRepository.findByName(name).orElseGet(() -> {
            var umUserPrivilege =  new UmUserPrivilege(name);
            return privilegeRepository.save(umUserPrivilege);
        });
    }

    @Transactional
    public UmUserRole createRoleIfNotFound(final String name,
                                           final Collection<UmUserPrivilege> privileges) {
       return roleRepository.findByName(name).orElseGet(() -> {
           var umUserRole = new UmUserRole(name);
           umUserRole.setUmUserPrivileges(privileges);
           return roleRepository.save(umUserRole);
       });
    }

    @Transactional
    public UmUser createUserIfNotFound(
            final String name,
            final String email,
            final String password,
            final Set<UmUserRole> roles,
            String secret,
            MfaType mfaType) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            var umUser = new UmUser(
                    name,email,
                    passwordEncoder.encode(password),
                    roles,
                    UmUserStatus.REGISTERED,
                    secret,
                    mfaType);
            return userRepository.save(umUser);
        });
    }

}