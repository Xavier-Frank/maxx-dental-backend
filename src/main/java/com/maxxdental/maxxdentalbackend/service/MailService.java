package com.maxxdental.maxxdentalbackend.service;

import com.maxxdental.maxxdentalbackend.model.GenericResponse;
import com.maxxdental.maxxdentalbackend.model.request.ContactUsEmailRequest;
import jakarta.mail.MessagingException;

public interface MailService {
    GenericResponse<Boolean> sendContactUsEmail(ContactUsEmailRequest contactUsEmailRequest);
    void sendContactUsAcknowledgment(String customerEmail,String companyEmail, String customerName, String originalSubject) throws MessagingException;
}
