package co.grtk.um.listener;

import co.grtk.um.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailEventListener implements ApplicationListener<MailEvent> {
    private final MailService mailService;

    @Override
    public void onApplicationEvent(MailEvent event) throws RuntimeException {
        try {
            if(MailType.REGISTRATION.equals(event.getMailType()))
                mailService.sendVerificationEmail(event.getPrincipal(), event.getApplicationUrl());
            else if(MailType.PASSWORD_RESET.equals(event.getMailType()))
                mailService.sendPasswordResetVerificationEmail(event.getPrincipal(), event.getApplicationUrl());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
