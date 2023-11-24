package com.bedmanagement.bedtracker.user.ui.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HospitalResponseModel {

    private long hospital_id;

    private String name;

    private String address;

    private String city;

    private String state;

    private int ZIP;

    private int noOfBedsAvailable;
}
