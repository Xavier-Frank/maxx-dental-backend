package com.maxxdental.maxxdentalbackend.utils;

import com.maxxdental.maxxdentalbackend.model.dto.CustomerNameDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Helpers {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public boolean isEmpty(String message) {
        return message == null || message.trim().isEmpty();
    }

    public String getCurrentTimeStamp() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public String generateContactUsMailBody() {

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Customer Inquiry</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 8px;\n" +
                "            background-color: #f9f9f9;\n" +
                "        }\n" +
                "        .header {\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            margin-top: 15px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 25px;\n" +
                "            font-size: 14px;\n" +
                "            color: #555;\n" +
                "        }\n" +
                "        .highlight {\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">New Customer Inquiry</div>\n" +
                "\n" +
                "        <div class=\"content\">\n" +
                "            <p>Hello Maxx Company Team,</p>\n" +
                "\n" +
                "            <p>You have received a new inquiry from a customer:</p>\n" +
                "\n" +
                "            <p><span class=\"highlight\">Customer Name:</span> {{CLIENT_NAME}}</p>\n" +
                "            <p><span class=\"highlight\">Customer Phone Number:</span> {{CLIENT_PHONE_NUMBER}}</p>\n" +
                "            <p><span class=\"highlight\">Message:</span></p>\n" +
                "            <p>{{CLIENT_MESSAGE}}</p>\n" +
                "\n" +
                "            <p>Please follow up with the customer as soon as possible.</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"footer\">\n" +
                "            This email was sent from your website contact form  by a client.\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

    }

    public CustomerNameDto extractCustomerNames(String fullName) {
        CustomerNameDto dto = new CustomerNameDto();

        if (fullName == null || fullName.trim().isEmpty()) {
            return dto; // return all fields null
        }

        // Split by one or more spaces and remove empty parts
        String[] parts = fullName.trim().split("\\s+");

        switch (parts.length) {
            case 1 -> dto.setFirstName(parts[0]);
            case 2 -> {
                dto.setFirstName(parts[0]);
                dto.setMiddleName(parts[1]);
            }
            case 3 -> {
                dto.setFirstName(parts[0]);
                dto.setMiddleName(parts[1]);
                dto.setLastName(parts[2]);
            }
            default -> {
                dto.setFirstName(parts[0]);
                dto.setMiddleName(parts[1]);
                // Join all remaining parts as lastName
                String lastName = String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length));
                dto.setLastName(lastName);
            }
        }

        return dto;
    }

    public String acknowledgmentOfContactUsMailBody() {

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Message Received</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "            background-color: #f5f5f5;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            border: 1px solid #ddd;\n" +
                "        }\n" +
                "        .header {\n" +
                "            font-size: 20px;\n" +
                "            font-weight: bold;\n" +
                "            margin-bottom: 15px;\n" +
                "            color: #2c3e50;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 25px;\n" +
                "            font-size: 14px;\n" +
                "            color: #555;\n" +
                "        }\n" +
                "        .highlight {\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">We Received Your Message!</div>\n" +
                "\n" +
                "        <div class=\"content\">\n" +
                "            <p>Hi {{CLIENT_NAME}},</p>\n" +
                "\n" +
                "            <p>Thanks for contacting <strong>{{COMPANY_NAME}}</strong>. \n" +
                "            We received your \"<strong>{{ORIGINAL_SUBJECT}}</strong>\" message, \n" +
                "            and our customer team is reviewing it.</p>\n" +
                "\n" +
                "            <p>Please respond to this email if you’d like to add any additional comments or details.</p>\n" +
                "\n" +
                "            <p>Best regards,<br/>\n" +
                "               {{AGENT_NAME}}<br/>\n" +
                "               {{COMPANY_NAME}}</p>\n" +
                "        </div>\n" +
                "\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
