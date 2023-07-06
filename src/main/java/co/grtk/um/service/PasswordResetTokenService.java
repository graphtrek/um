package co.grtk.um.service;

import co.grtk.um.exception.InvalidPasswordResetTokenException;
import co.grtk.um.model.PasswordResetToken;
import co.grtk.um.model.User;
import co.grtk.um.model.VerificationToken;
import co.grtk.um.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private static final String TOKEN_NOT_FOUND= "PasswordRestToken Not Found";
    @Transactional
    public PasswordResetToken createPasswordResetTokenForUser(User user, String passwordToken) {
        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordToken, user);
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
    public PasswordResetToken validatePasswordResetToken(String passwordResetToken) {
        PasswordResetToken passwordToken =
                passwordResetTokenRepository.
                        findByToken(passwordResetToken).
                        orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));

        Calendar calendar = Calendar.getInstance();
        if ((passwordToken.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            throw new InvalidPasswordResetTokenException("PasswordRestToken expired");
        }
        return passwordToken;
    }
    public User findUserByPasswordToken(String passwordResetToken) {
        return passwordResetTokenRepository.
                findByToken(passwordResetToken).
                orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND)).getUser();
    }

    public PasswordResetToken findPasswordResetToken(String token){
      return passwordResetTokenRepository.
              findByToken(token).orElseThrow(() -> new InvalidPasswordResetTokenException(TOKEN_NOT_FOUND));
    }
}
