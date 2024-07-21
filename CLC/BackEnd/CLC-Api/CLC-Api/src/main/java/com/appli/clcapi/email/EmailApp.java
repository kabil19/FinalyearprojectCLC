package com.appli.clcapi.email;

import io.jsonwebtoken.io.IOException;
import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@RestController
@RequestMapping("/send-email")
public class EmailApp {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public <MimeMessage> ResponseEntity<String> sendEmailWithAttachment( @RequestParam("file") MultipartFile file,
                                                                         @RequestParam("mail") String emailId,
                                                                         @RequestParam("title") String title,
                                                                         @RequestParam("subject") String subject
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file provided");
            }
            if (emailId == null || emailId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("No recipient email provided");
            }
            MimeMessage message = (MimeMessage) mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper((jakarta.mail.internet.MimeMessage) message, true);

            helper.setTo(emailId);
            helper.setSubject("PDF Document :"+subject);
            helper.setText(title);
            helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);

            mailSender.send((jakarta.mail.internet.MimeMessage) message);

            return ResponseEntity.status(200).body("Email is Successfully sent!");

        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }
}
