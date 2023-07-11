package co.grtk.um.service;

import co.grtk.um.exception.UserAlreadyExistsException;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.PrincipalStatus;
import co.grtk.um.model.VerificationToken;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationService {
    private final UmUserRepository umUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpService totpService;


    @Transactional
    public VerificationToken registerUser(UmUser umUser) throws UserAlreadyExistsException {
        umUserRepository.findByEmail(umUser.getEmail()).ifPresent(user1 -> {
            throw new UserAlreadyExistsException("User Already Exists");
        });
        umUser.setPassword(passwordEncoder.encode(umUser.getPassword()));
        umUser.setRoles("ROLE_USER");
        umUser.setStatus(PrincipalStatus.PENDING);
        umUser.setSecret(totpService.generateSecret());
        umUserRepository.save(umUser);

        var verificationToken = new VerificationToken(UUID.randomUUID().toString(), umUser);
        verificationTokenRepository.save(verificationToken);
        log.info("Saved verificationToken: {}", verificationToken.getToken());
        return verificationToken;
    }

    @Transactional
    public void resetPassword(UmUser theUmUser, String newPassword) {
        theUmUser.setPassword(passwordEncoder.encode(newPassword));
        umUserRepository.save(theUmUser);
    }
}
