package com.bedmanagement.bedtracker.io.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Hospital {

    @Id
    private long hospital_id;

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, length=50)
    private String address;

    @Column(nullable=false, length=120)
    private String city;

    @Column(nullable=false)
    private String State;

    @Column(nullable=false)
    private Integer ZIP;

    private Integer noOfBedsAvailable;

    @OneToOne(mappedBy="hospital")
    private Patient patient;





}
