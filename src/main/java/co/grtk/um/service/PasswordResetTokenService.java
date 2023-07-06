package co.grtk.um.service;

import co.grtk.um.exception.InvalidPasswordResetTokenException;
import co.grtk.um.model.PasswordResetToken;
import co.grtk.um.model.User;
import co.grtk.um.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private static final String TOKEN_NOT_FOUND= "";
    public PasswordResetToken createPasswordResetTokenForUser(User user, String passwordToken) {
        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordToken, user);
        return passwordResetTokenRepository.save(passwordRestToken);
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
