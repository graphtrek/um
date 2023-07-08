package co.grtk.um.service;

import co.grtk.um.exception.UserAlreadyExistsException;
import co.grtk.um.model.Principal;
import co.grtk.um.model.PrincipalStatus;
import co.grtk.um.model.VerificationToken;
import co.grtk.um.repository.PrincipalRepository;
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
    private final PrincipalRepository principalRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpService totpService;


    @Transactional
    public VerificationToken registerUser(Principal principal) throws UserAlreadyExistsException {
        principalRepository.findByEmail(principal.getEmail()).ifPresent(user1 -> {
            throw new UserAlreadyExistsException("User Already Exists");
        });
        principal.setPassword(passwordEncoder.encode(principal.getPassword()));
        principal.setRoles("ROLE_USER");
        principal.setStatus(PrincipalStatus.PENDING);
        principal.setSecret(totpService.generateSecret());
        principalRepository.save(principal);

        var verificationToken = new VerificationToken(UUID.randomUUID().toString(), principal);
        verificationTokenRepository.save(verificationToken);
        log.info("Saved verificationToken: {}", verificationToken.getToken());
        return verificationToken;
    }

    @Transactional
    public void resetPassword(Principal thePrincipal, String newPassword) {
        thePrincipal.setPassword(passwordEncoder.encode(newPassword));
        principalRepository.save(thePrincipal);
    }
}
