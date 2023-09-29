package co.grtk.um.service;

import co.grtk.um.config.AppConfig;
import co.grtk.um.exception.InvalidTwoFactorVerificationCode;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@Slf4j
public class TotpService {

    private final AppConfig appConfig;
    private final TimeProvider timeProvider;
    private final DefaultCodeGenerator codeGenerator;

    private final DefaultCodeVerifier verifier;
    public TotpService(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.timeProvider = new SystemTimeProvider();
        this.codeGenerator = new DefaultCodeGenerator();
        this.verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    }

    public String generateSecret() {
        SecretGenerator generator = new DefaultSecretGenerator();
        return generator.generate();
    }

    public String getUriForImage(String secret, String userName) {
        QrData data = new QrData.Builder()
                .label(userName)
                .secret(secret)
                .issuer(appConfig.getApplicationName())
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];

        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
           log.error("unable to generate QrCode");
        }

        String mimeType = generator.getImageMimeType();

        return getDataUriForImage(imageData, mimeType);
    }


    public String generateCode(String secret, int timePeriod) throws CodeGenerationException {
        long currentBucket = Math.floorDiv(timeProvider.getTime(), timePeriod);
        return codeGenerator.generate(secret, currentBucket);
    }
    public void verifyCode(String code, String secret) {
        if(!verifier.isValidCode(secret, code)) {
            throw new InvalidTwoFactorVerificationCode("Invalid 2FA code:" + code);
        }
    }

    public boolean isValidCode(String secret, String code, int timePeriod) {
        verifier.setTimePeriod(timePeriod);
        return verifier.isValidCode(secret, code);
    }

}
