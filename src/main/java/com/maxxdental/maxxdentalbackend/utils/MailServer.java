package com.maxxdental.maxxdentalbackend.utils;

import jakarta.annotation.Nullable;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class MailServer {
    private static final Logger log = LoggerFactory.getLogger(MailServer.class);
    private final JavaMailSender mailSender;
    private final Helpers helpers;


    public void sendEmail(String to, String from, String subject, String body,
                          @Nullable byte[] attachment, @Nullable String attachmentName, Boolean htmlContent) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        boolean hasAttachment = attachment != null && !helpers.isEmpty(attachmentName);

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, hasAttachment, StandardCharsets.UTF_8.name());

        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, htmlContent);

        if (hasAttachment) {
            InputStreamSource stream = new ByteArrayResource(attachment);

            mimeMessageHelper.addAttachment(attachmentName, stream);
        }

        try {
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Unable to send email: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }


    public void sendContactUsAcknowledgement(String customerEmail, String companyEmail, String customerName, String originalSubject, String companyName, String agentName) throws MessagingException {
        String subject = companyName + " " + originalSubject;
        String htmlTemplate = helpers.acknowledgmentOfContactUsMailBody();

        String body = htmlTemplate
                .replace("{{CLIENT_NAME}}", customerName)
                        .replace("{{COMPANY_NAME}}", companyName)
                                .replace("{{AGENT_NAME}}", agentName)
                                        .replace("{{ORIGINAL_SUBJECT}}", subject);

        sendEmail(customerEmail, companyEmail, subject, body, null, null, true);
    }
}
