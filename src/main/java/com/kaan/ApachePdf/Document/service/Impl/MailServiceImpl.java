package com.kaan.ApachePdf.Document.service.Impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String recipientEmail, String verificationLink) {
        String subject = "PDF Doğrulama";

        String body = "<html><body>"
                + "<h2>Merhaba,</h2>"
                + "<p>PDF'inizi indirmek için aşağıdaki bağlantıya tıklayın:</p>"
                + "<p><a href='" + verificationLink + "' style='color:blue; font-weight:bold;'>PDF'yi İndir</a></p>"
                + "<br><p>İyi günler dileriz.</p>"
                + "</body></html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            logger.info("Doğrulama e-postası başarıyla gönderildi: {}", recipientEmail);


        } catch (MailException | MessagingException e) {
            logger.error("E-posta gönderme hatası: {}", e.getMessage());
        }
    }
}
