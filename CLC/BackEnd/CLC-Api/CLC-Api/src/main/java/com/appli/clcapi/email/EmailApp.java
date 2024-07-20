package com.appli.clcapi.email;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/send-email")
public class EmailApp {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public <MimeMessage> ResponseEntity<String> sendEmailWithAttachment(@RequestParam("file") MultipartFile file) {
        try {
            MimeMessage message = (MimeMessage) mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper((jakarta.mail.internet.MimeMessage) message, true);

            helper.setTo("dabinawan@gmail.com");
            helper.setSubject("Subject: Your PDF Document");
            helper.setText("Please find the attached PDF document.");
            helper.addAttachment(file.getOriginalFilename(), file);

            mailSender.send((jakarta.mail.internet.MimeMessage) message);

            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }
}
