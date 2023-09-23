package co.grtk.um.service;

import co.grtk.um.exception.UmException;
import co.grtk.um.exception.UserAlreadyExistsException;
import co.grtk.um.model.*;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.repository.RegistrationTokenRepository;
import co.grtk.um.repository.UmUserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationService {
    private final UmUserRepository umUserRepository;
    private final UmUserRoleRepository umUserRoleRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpService totpService;
    private static final int TIME_PERIOD_MINUTES = 30;

    @Transactional
    public RegistrationToken registerUser(UmUser umUser) throws UserAlreadyExistsException {
        umUserRepository.findByEmail(umUser.getEmail()).ifPresent(user1 -> {
            throw new UserAlreadyExistsException("User Already Exists " + umUser.getEmail());
        });

        UmUserRole userRole = umUserRoleRepository.findByName("ROLE_USER").orElseThrow(
                () -> new UmException("Inconsistent Database UmUserRole USER does not exists")
        );

        umUser.setPassword(passwordEncoder.encode(umUser.getPassword()));
        umUser.setRoles(Set.of(userRole));
        umUser.setStatus(UmUserStatus.PENDING);
        umUser.setSecret(totpService.generateSecret());
        umUserRepository.save(umUser);

        var verificationToken = new RegistrationToken(UUID.randomUUID().toString(), TIME_PERIOD_MINUTES, umUser);
        registrationTokenRepository.save(verificationToken);
        log.info("Saved verificationToken: {}", verificationToken.getToken());
        return verificationToken;
    }

    @Transactional
    public void resetPassword(UmUser theUmUser, String newPassword) {
        theUmUser.setPassword(passwordEncoder.encode(newPassword));
        umUserRepository.save(theUmUser);
    }
}
