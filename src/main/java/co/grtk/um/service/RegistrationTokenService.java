package co.grtk.um.service;

import co.grtk.um.exception.InvalidPasswordResetTokenException;
import co.grtk.um.exception.InvalidRegistrationTokenException;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.UmUserStatus;
import co.grtk.um.model.RegistrationToken;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.repository.RegistrationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationTokenService {
    private final RegistrationTokenRepository registrationTokenRepository;
    private final UmUserRepository umUserRepository;
    private static final int TIME_PERIOD_MINUTES = 30;
    @Transactional
    public void validateToken(String token) {
        log.info("Received token: {}", token);
        RegistrationToken registrationToken =
                registrationTokenRepository.findByToken(token).
                        orElseThrow(() ->
                                new InvalidPasswordResetTokenException("Invalid verification token:" + token));

        UmUser umUser = registrationToken.getUmUser();

        Instant now = Instant.now();
        if (now.isAfter(registrationToken.getTokenExpirationTime())){
            throw new InvalidRegistrationTokenException("Invalid verification token:" + token);
        }
        umUser.setStatus(UmUserStatus.REGISTERED);
        umUserRepository.save(umUser);
    }

    @Transactional
    public RegistrationToken generateNewRegistrationToken(String oldToken) {
        RegistrationToken registrationToken =
                registrationTokenRepository.findByToken(oldToken).
                        orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid registration oldToken:" + oldToken));

        registrationToken.setToken(UUID.randomUUID().toString());
        Instant now = Instant.now();
        Instant expiration = now.plus(TIME_PERIOD_MINUTES, ChronoUnit.MINUTES);
        registrationToken.setIssuedAtUtcTime(expiration);
        return registrationTokenRepository.save(registrationToken);
    }

    public List<RegistrationToken> loadAllRegistrationTokens(){
        return registrationTokenRepository.findAll();
    }

}
