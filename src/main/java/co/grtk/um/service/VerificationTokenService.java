package co.grtk.um.service;

import co.grtk.um.exception.InvalidVerificationTokenException;
import co.grtk.um.model.User;
import co.grtk.um.model.UserStatus;
import co.grtk.um.model.VerificationToken;
import co.grtk.um.repository.UserRepository;
import co.grtk.um.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void validateToken(String token) {
        log.info("Received token: {}", token);
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
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
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return verificationTokenRepository.save(verificationToken);
    }

}
