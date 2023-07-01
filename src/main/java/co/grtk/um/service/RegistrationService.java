package co.grtk.um.service;

import co.grtk.um.exception.InvalidVerificationTokenException;
import co.grtk.um.exception.UserAlreadyExistsException;
import co.grtk.um.model.User;
import co.grtk.um.model.UserStatus;
import co.grtk.um.model.VerificationToken;
import co.grtk.um.repository.UserRepository;
import co.grtk.um.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public VerificationToken registerUser(User user) throws UserAlreadyExistsException {
        userRepository.findByEmail(user.getEmail()).ifPresent(user1 -> {
            throw new UserAlreadyExistsException("User Already Exists");
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        user.setUserStatus(UserStatus.PENDING);
        userRepository.save(user);

        var verificationToken = new VerificationToken(UUID.randomUUID().toString(), user);
        tokenRepository.save(verificationToken);
        log.info("Saved verificationToken: {}", verificationToken.getToken());
        return verificationToken;
    }

    @Transactional
    public void validateToken(String token) {
        log.info("Received token: {}", token);
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken == null){
            throw new InvalidVerificationTokenException("Invalid verification token:" + token);
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            throw new InvalidVerificationTokenException("Invalid verification token:" + token);
        }
        user.setUserStatus(UserStatus.REGISTERED);
        userRepository.save(user);
    }

    @Transactional
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = tokenRepository.findByToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);
    }


}
