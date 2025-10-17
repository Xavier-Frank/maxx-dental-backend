package com.maxxdental.maxxdentalbackend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericResponse<T> {

    private ResponseHeader responseHeader;
    private T responseBody;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseHeader {
        private int responseCode;
        private String responseRefId;
        private String customerMessage;
        private String debugMessage;
    }
}
