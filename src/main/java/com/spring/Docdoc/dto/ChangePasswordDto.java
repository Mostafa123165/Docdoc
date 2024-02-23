package com.spring.Docdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.thymeleaf.util.StringUtils;

@Data
@AllArgsConstructor
public class ChangePasswordDto {

    private String email ;
    private String newPassword ;
    private String confirmNewPassword ;

    public boolean isValid() {
        return StringUtils.equals(newPassword,confirmNewPassword);
    }
}
