package com.bedmanagement.bedtracker.user.ui.model.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class PatientForm {

    @Size(min = 1, max = 3, message = "age length should be between 1 and 13")
    @NotBlank(message = "age should not be empty")
    private String age;

    @Min(value = 2, message = "Min days required-2")
    @Max(value = 4, message = "Max days required:4")
    @NotNull(message = "daysRequired should not be empty")
    private int daysRequired;

    @NotNull(message = "hospital id should not be empty")
    private long hospital_id;

}
