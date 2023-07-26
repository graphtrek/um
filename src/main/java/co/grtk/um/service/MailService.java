package co.grtk.um.service;

import co.grtk.um.model.UmUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendRegistrationEmail(UmUser umUser, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String mailContent = "<p> Hi, "+ umUser.getName() + ", </p>"+
                "<p>Thank you for registering with us," +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
       sendMail(mailContent, umUser,subject);
       log.info("Click the link to verify your registration :  {}", url);
    }


    public void sendPasswordResetEmail(UmUser umUser, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String mailContent = "<p> Hi, "+ umUser.getName()+ ", </p>"+
                "<p><b>You recently requested to reset your password,</b>" +
                "Please, follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> Users Registration Portal Service";
        sendMail(mailContent, umUser,subject);
        log.info("Click the link to verify your registration :  {}", url);
    }

    private void sendMail(String mailContent, UmUser umUser, String subject)  throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("graphtrek@gmail.com","User Registration Portal Service");
        messageHelper.setTo(umUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}
