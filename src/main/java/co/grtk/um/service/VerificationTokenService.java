package co.grtk.um.service;

import co.grtk.um.exception.InvalidPasswordResetTokenException;
import co.grtk.um.exception.InvalidVerificationTokenException;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.PrincipalStatus;
import co.grtk.um.model.VerificationToken;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UmUserRepository umUserRepository;

    @Transactional
    public void validateToken(String token) {
        log.info("Received token: {}", token);
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token).
                        orElseThrow(() ->
                                new InvalidPasswordResetTokenException("Invalid verification token:" + token));

        UmUser umUser = verificationToken.getUmUser();

        Instant now = Instant.now();
        if (now.isAfter(verificationToken.getTokenExpirationTime())){
            throw new InvalidVerificationTokenException("Invalid verification token:" + token);
        }
        umUser.setStatus(PrincipalStatus.REGISTERED);
        umUserRepository.save(umUser);
    }

    @Transactional
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(oldToken).
                        orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid verification oldToken:" + oldToken));
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return verificationTokenRepository.save(verificationToken);
    }

}
