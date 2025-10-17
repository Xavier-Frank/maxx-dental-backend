package com.maxxdental.maxxdentalbackend.service.impl;

import com.maxxdental.maxxdentalbackend.model.GenericResponse;
import com.maxxdental.maxxdentalbackend.model.dto.CustomerNameDto;
import com.maxxdental.maxxdentalbackend.model.request.ContactUsEmailRequest;
import com.maxxdental.maxxdentalbackend.service.MailService;
import com.maxxdental.maxxdentalbackend.utils.Helpers;
import com.maxxdental.maxxdentalbackend.utils.MailServer;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MailServiceImplementation implements MailService {
    private static final Logger log = LoggerFactory.getLogger(MailServiceImplementation.class);
    private final MailServer mailServer;
    private final Helpers helpers;
    private final Environment environment;

    @Override
    public GenericResponse<Boolean> sendContactUsEmail(ContactUsEmailRequest contactUsEmailRequest) {
        log.info("Sending contact us email from {}...", !helpers.isEmpty(contactUsEmailRequest.getClientEmailAddress()) ? contactUsEmailRequest.getClientEmailAddress() : "N/A");

        //validate request body
        GenericResponse<Boolean> validatedRequestBody = validateRequestBody(contactUsEmailRequest);

        if (validatedRequestBody.getResponseHeader().getResponseCode() == HttpStatus.OK.value()) {

            String emailBody = helpers.generateContactUsMailBody();

            CustomerNameDto customerName = helpers.extractCustomerNames(contactUsEmailRequest.getName());


            String body = emailBody
                    .replace("{{CLIENT_NAME}}", customerName.getFirstName() + " " + (!helpers.isEmpty(customerName.getMiddleName()) ? customerName.getMiddleName() : ""))
                    .replace("{{CLIENT_MESSAGE}}", contactUsEmailRequest.getMessage())
                    .replace("{{CLIENT_PHONE_NUMBER}}", contactUsEmailRequest.getPhoneNumber());

            //send email
            try{
                String subject = "Client Inquiry";

                mailServer.sendEmail(environment.getProperty("spring.mail.username"), contactUsEmailRequest.getClientEmailAddress(),
                        subject,
                        body, null, null, true);


                //return acknowledgement
                CompletableFuture.runAsync(() -> {
                    try {
                        sendContactUsAcknowledgment(
                                contactUsEmailRequest.getClientEmailAddress(),
                                environment.getProperty("mail.company.email"),
                                customerName.getFirstName(),
                                "Acknowledgement"
                        );
                    } catch (MessagingException e) {
                        log.warn("Unable to send acknowledgment email: {}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                });



                return GenericResponse.<Boolean>builder()
                        .responseHeader(GenericResponse.ResponseHeader.builder()
                                .responseCode(HttpStatus.OK.value())
                                .customerMessage("Email sent successfully")
                                .debugMessage("Successfully sent email")
                                .responseRefId(validatedRequestBody.getResponseHeader().getResponseRefId())
                                .build())
                        .responseBody(true)
                        .build();



            } catch (Exception e) {
                return GenericResponse.<Boolean>builder()
                        .responseHeader(GenericResponse.ResponseHeader.builder()
                                .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .customerMessage("Unable to send message, please try again later")
                                .debugMessage(e.getMessage())
                                .responseRefId(validatedRequestBody.getResponseHeader().getResponseRefId())
                                .build())
                        .responseBody(false)
                        .build();
            }


        }

        return validatedRequestBody;
    }

    @Override
    public void sendContactUsAcknowledgment(String customerEmail, String companyEmail, String customerName, String originalSubject) throws MessagingException {
        mailServer.sendContactUsAcknowledgement(customerEmail,companyEmail, customerName, originalSubject, environment.getProperty("mail.company.name"), environment.getProperty("mail.company.agent"));
    }

    private GenericResponse<Boolean> validateRequestBody(ContactUsEmailRequest contactUsEmailRequest) {

        if (contactUsEmailRequest == null) {
            return GenericResponse.<Boolean>builder()
                    .responseHeader(GenericResponse.ResponseHeader.builder()
                            .responseCode(HttpStatus.BAD_REQUEST.value())
                            .responseRefId(helpers.getCurrentTimeStamp())
                            .customerMessage("Mail request body is missing")
                            .debugMessage("Mail request body is missing. Check payload and try again")
                            .build())
                    .responseBody(false)
                    .build();
        }

        Map<String, String> fieldsToValidate = Map.of(
                contactUsEmailRequest.getClientEmailAddress(),"Client Email address",
                contactUsEmailRequest.getMessage(), "Client Message",
                contactUsEmailRequest.getPhoneNumber(), "Client Phone number",
                contactUsEmailRequest.getName(), "Client Name"
        );

        for (Map.Entry<String, String> entry : fieldsToValidate.entrySet()) {
            if (helpers.isEmpty(entry.getKey())) {
                return GenericResponse.<Boolean>builder()
                        .responseHeader(GenericResponse.ResponseHeader.builder()
                                .responseCode(HttpStatus.BAD_REQUEST.value())
                                .responseRefId(helpers.getCurrentTimeStamp())
                                .customerMessage(entry.getValue() + " " + "is missing but is required")
                                .debugMessage(entry.getValue() + " " + "is missing but is required")
                                .build())
                        .responseBody(false)
                        .build();
            }
        }

        return GenericResponse.<Boolean>builder()
                .responseHeader(GenericResponse.ResponseHeader.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseRefId(helpers.getCurrentTimeStamp())
                        .customerMessage("Request body is valid")
                        .debugMessage("Request body is valid")
                        .build())
                .responseBody(true)
                .build();

    }
}
