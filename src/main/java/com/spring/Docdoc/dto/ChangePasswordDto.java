package com.spring.Docdoc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.thymeleaf.util.StringUtils;

@Data
@AllArgsConstructor
public class ChangePasswordDto {

    @NotBlank(message = "email is required")
    private String email ;

    @NotBlank(message = "newPassword is required")
    private String newPassword ;

    @NotBlank(message = "confirmNewPassword is required")
    private String confirmNewPassword ;

    public boolean isValid() {

        return StringUtils.equals(newPassword,confirmNewPassword);
    }
}
