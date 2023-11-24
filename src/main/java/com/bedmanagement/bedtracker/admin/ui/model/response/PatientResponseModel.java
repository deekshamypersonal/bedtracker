package com.bedmanagement.bedtracker.admin.ui.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PatientResponseModel {

    private long id;

    private String age;

    private int daysRequired;

    private String email;

    private LocalDateTime startDate;



}
