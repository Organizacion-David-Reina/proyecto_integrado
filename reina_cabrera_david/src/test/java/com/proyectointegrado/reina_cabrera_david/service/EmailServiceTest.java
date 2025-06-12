package com.proyectointegrado.reina_cabrera_david.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendEmail_Success() throws Exception {
        String to = "test@example.com";
        String subject = "Test Subject";
        String htmlContent = "<h1>Hello World</h1>";

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertDoesNotThrow(() -> emailService.sendEmail(to, subject, htmlContent));

        verify(javaMailSender).send(Mockito.any(MimeMessage.class));
    }
}

