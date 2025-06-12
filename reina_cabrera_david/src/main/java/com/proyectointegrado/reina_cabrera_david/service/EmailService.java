package com.proyectointegrado.reina_cabrera_david.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * The Class EmailService
 */
@Service
public class EmailService {

	/** The mail sender */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send the email to someone
     * 
     * @param to 
     * 		the person who receives the email
     * @param subject
     * 		the subject
     * @param htmlContent
     * 		the html content
     */
    public void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("stepfloworganization@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
}

