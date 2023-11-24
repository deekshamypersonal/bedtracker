package com.bedmanagement.bedtracker.admin.ui.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class HospitalRequestModel {

    private long hospital_id;

    private long ZIP;

    @NotBlank(message = "Hospital Name is mandatory")
    @Size(max = 30, message = "Max length allowed 30")
    private String name;

    @NotBlank(message = "Hospital Address is mandatory")
    @Size(max = 30, message = "Max length allowed 30")
    private String address;

    @NotBlank(message = "Hospital Address is mandatory")
    @Size(max = 30, message = "Max length allowed 30")
    private String city;

    @NotBlank(message = "Hospital Address is mandatory")
    @Size(max = 30, message = "Max length allowed 30")
    private String state;

    @NotNull(message = "noOfBedsAvailable is mandatory")
    private int noOfBedsAvailable;
}
