package co.grtk.um.service;

import co.grtk.um.exception.InvalidPasswordResetTokenException;
import co.grtk.um.model.PasswordResetToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private static final int TIME_PERIOD_MINUTES = 30;
    private static final String TOKEN_NOT_FOUND= "PasswordRestToken Not Found";
    @Transactional
    public PasswordResetToken createPasswordResetTokenForUser(UmUser umUser, String passwordToken) {
        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordToken,TIME_PERIOD_MINUTES, umUser);
        return passwordResetTokenRepository.save(passwordRestToken);
    }

    @Transactional
    public PasswordResetToken generateNewPasswordResetTokenFor(String oldToken) {
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.
                        findByToken(oldToken).orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));

        passwordResetToken.setToken(UUID.randomUUID().toString());
        Instant now = Instant.now();
        Instant expiration = now.plus(TIME_PERIOD_MINUTES, ChronoUnit.MINUTES);

        passwordResetToken.setIssuedAtUtcTime(expiration);
        return passwordResetTokenRepository.save(passwordResetToken);
    }
    public void validatePasswordResetToken(String passwordResetToken) {
        PasswordResetToken passwordToken =
                passwordResetTokenRepository.
                        findByToken(passwordResetToken).
                        orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));

        Instant now = Instant.now();
        if (now.isAfter(passwordToken.getTokenExpirationTime())){
            throw new InvalidPasswordResetTokenException("PasswordRestToken expired");
        }
    }
    public UmUser findUserByPasswordToken(String passwordResetToken) {
        return passwordResetTokenRepository.
                findByToken(passwordResetToken).
                orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND)).getUmUser();
    }

    public PasswordResetToken findPasswordResetToken(String token){
      return passwordResetTokenRepository.
              findByToken(token).orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));
    }
}
