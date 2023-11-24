package com.bedmanagement.bedtracker.io.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(nullable=false, length=50)
    private String identifier;

    @Column(nullable=false, length=50)
    private String firstName;

    @Column(nullable=false, length=50)
    private String lastName;

    @Column(nullable=false, length=120,unique=true)
    private String email;

    @Column(nullable=false)
    private String encryptedPassword;

    @Column(nullable=false)
    private String role;

    @OneToOne(mappedBy="user",cascade=CascadeType.REMOVE)
    private Patient patient;
}
