package com.bedmanagement.bedtracker.admin.ui.model.request;

import com.bedmanagement.bedtracker.common.ApprovalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AllocationStatusModel {

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotBlank(message = "Email is mandatory")
    @Size(max = 120, message = "email size too long")
    private String email;


    private ApprovalStatus status;

    @Size(max = 120, message = "reason size too long")
    @NotBlank(message = "Reason is mandatory")
    private String reason;


}
