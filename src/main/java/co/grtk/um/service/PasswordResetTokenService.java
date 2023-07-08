package co.grtk.um.service;

import co.grtk.um.exception.InvalidPasswordResetTokenException;
import co.grtk.um.model.PasswordResetToken;
import co.grtk.um.model.Principal;
import co.grtk.um.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private static final String TOKEN_NOT_FOUND= "PasswordRestToken Not Found";
    @Transactional
    public PasswordResetToken createPasswordResetTokenForUser(Principal principal, String passwordToken) {
        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordToken, principal);
        return passwordResetTokenRepository.save(passwordRestToken);
    }

    @Transactional
    public PasswordResetToken generateNewPasswordResetTokenFor(String oldToken) {
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.
                        findByToken(oldToken).orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));
        var passwordResetTokenTime = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpirationTime(passwordResetTokenTime.getTokenExpirationTime());
        return passwordResetTokenRepository.save(passwordResetToken);
    }
    public void validatePasswordResetToken(String passwordResetToken) {
        PasswordResetToken passwordToken =
                passwordResetTokenRepository.
                        findByToken(passwordResetToken).
                        orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));

        Instant now = Instant.now();
        if (now.isAfter(passwordToken.getExpirationTime())){
            throw new InvalidPasswordResetTokenException("PasswordRestToken expired");
        }
    }
    public Principal findUserByPasswordToken(String passwordResetToken) {
        return passwordResetTokenRepository.
                findByToken(passwordResetToken).
                orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND)).getPrincipal();
    }

    public PasswordResetToken findPasswordResetToken(String token){
      return passwordResetTokenRepository.
              findByToken(token).orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));
    }
}
