package com.bedmanagement.bedtracker.security;

import com.bedmanagement.bedtracker.common.Constants;
import com.bedmanagement.bedtracker.exception.ErrorMessageConstant;
import com.bedmanagement.bedtracker.exception.UserServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationService {

    @Autowired
    private JavaMailSender mailSender;

    final String HTMLBODY = "<h1>Please verify your email address</h1>"
            + "<p>Thank you for registering with our mobile app. To complete registration process and be able to log in,"
            + " click on the following link: "
            + "<a href='http://localhost:8080/bedtracker/email-verification?token=$tokenValue'>"
            + "Final step to complete your registration" + "</a><br/><br/>"
            + "Thank you! And we are waiting for you inside!";

    final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password</h1>"
            + "<p>Hi, $firstName!</p> "
            + "<p>Someone has requested to reset your password with our project. If it were not you, please ignore it."
            + " otherwise please click on the link below to set a new password: "
            + "<a href='http://localhost:8080/bedtracker/reset-pw?token=$tokenValue'>"
            + " Click this link to Reset Password"
            + "</a><br/><br/>"
            + "Thank you!";


    public void sendRegisterUserMail(String to, String emailToken) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", emailToken);
            message.setSubject("Verify Email");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(Constants.SYSTEM_EMAIL);
            helper.setTo(to);
            helper.setText(htmlBodyWithToken, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new UserServiceException("Error while sending mail");
        }catch (Exception ex) {
            throw new UserServiceException(ErrorMessageConstant.SERVER_ERROR_MSG);
        }
    }

    public void sendPaswordResetMail(String to, String emailToken) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", emailToken);
            message.setSubject("Reset Password");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(Constants.SYSTEM_EMAIL);
            helper.setTo(to);
            helper.setText(htmlBodyWithToken, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new UserServiceException("Error while sending mail");
        }catch (Exception ex) {
            throw new UserServiceException(ErrorMessageConstant.SERVER_ERROR_MSG);
        }
    }





}
