package com.maxxdental.maxxdentalbackend.controller;

import com.maxxdental.maxxdentalbackend.model.GenericResponse;
import com.maxxdental.maxxdentalbackend.model.request.ContactUsEmailRequest;
import com.maxxdental.maxxdentalbackend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("contact-us/send")
    public GenericResponse<Boolean> sendContactUsEmail(@RequestBody ContactUsEmailRequest sendEmailRequest){
        return mailService.sendContactUsEmail(sendEmailRequest);
    }
}
