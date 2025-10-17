package com.maxxdental.maxxdentalbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ContactUsEmailRequest {
    private String name;
    private String clientEmailAddress;
    private String phoneNumber;
    private String message;
}
