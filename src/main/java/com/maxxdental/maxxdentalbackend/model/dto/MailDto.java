package com.maxxdental.maxxdentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MailDto {
    private String from;
    private String to;
    private String subject;
    private String body;
}
