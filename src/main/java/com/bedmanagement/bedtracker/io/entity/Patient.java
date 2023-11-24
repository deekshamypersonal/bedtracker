package com.bedmanagement.bedtracker.io.entity;

import com.bedmanagement.bedtracker.common.ApprovalStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false, length=3)
    private String age;

    @Column(nullable=false, length=1)
    private int daysRequired;

    @Column(unique=true)
    private UUID uniqueId;

    @Column(nullable=false, length=120,unique=true)
    private String email;

    private String patientArrived;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="hospital_id")
    private Hospital hospital;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus HosptialAllocatedStatus;

    @Lob
    @Column(name = "prescriptionImage", columnDefinition="MEDIUMBLOB")
    private byte[] prescriptionImage;


}
