package com.maxxdental.maxxdentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerNameDto {
    private String firstName;
    private String middleName;
    private String lastName;
}
