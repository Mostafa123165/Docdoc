package com.spring.Docdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NotificationEmailDto {
    private String recipient ;
    private String subject ;
}
