package co.grtk.um.service;

import co.grtk.um.config.ApplicationConfig;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@RequiredArgsConstructor
@Service
@Slf4j
public class TotpService {

    private final ApplicationConfig applicationConfig;
    TimeProvider timeProvider = new SystemTimeProvider();
    DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

    public String generateSecret() {
        SecretGenerator generator = new DefaultSecretGenerator();
        return generator.generate();
    }

    public String getUriForImage(String secret) {
        QrData data = new QrData.Builder()
                .label("Two-factor-auth-test")
                .secret(secret)
                .issuer(applicationConfig.getName())
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
    public boolean verifyCode(String code, String secret) {
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }

    public boolean isValidCode(String secret, String code, int timePeriod) {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setTimePeriod(timePeriod);
        return verifier.isValidCode(secret, code);
    }

}
