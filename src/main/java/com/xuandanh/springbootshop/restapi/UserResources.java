package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.User;
import com.xuandanh.springbootshop.jwt.AuthEntryPointJwt;
import com.xuandanh.springbootshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
public class UserResources {
    private final UserService userService;
    private final JavaMailSender emailSender;
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    public UserService getUserService() {
        return userService;
    }

    public JavaMailSender getEmailSender() {
        return emailSender;
    }

    public UserResources(UserService userService, JavaMailSender emailSender){
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) throws MessagingException, UnsupportedEncodingException  {

        String response = userService.forgotPassword(email);

        if (!response.startsWith("Invalid")) {
            response = "http://localhost:8080/api/reset-password?token=" + response;
            sendEmail(email, response);
        }
        return response;
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password) {

        return userService.resetPassword(token, password);
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@shopme.com", "Spring team Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        emailSender.send(message);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?>findUser(@PathVariable("id")Long id){
        User user = userService.findOne(id);
        return ResponseEntity.ok(user);
    }
}
