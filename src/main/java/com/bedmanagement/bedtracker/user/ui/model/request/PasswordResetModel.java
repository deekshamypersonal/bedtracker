package com.bedmanagement.bedtracker.user.ui.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResetModel {

    @Size(min = 5, max = 12,message="Password should be between 5 and 12")
    @NotBlank(message = "Password is mandatory")
    private String newPassword;

    @Size(min = 5, max = 12,message="Password should be between 5 and 12")
    @NotBlank(message = "Password is mandatory")
    private String confirmationPassword;



}
