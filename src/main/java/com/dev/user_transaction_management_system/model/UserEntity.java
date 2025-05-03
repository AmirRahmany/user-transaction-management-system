package com.dev.user_transaction_management_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "transaction_user")
@Entity
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @Column(name = "first_name")
    private  String firstName;

    @Column(name = "last_name")
    private  String lastName;

    @Column(name = "phone_number")
    private  String phoneNumber;

    @Column(name = "email")
    private  String email;

    @Column(name = "password")
    private  String password;

    @Column(name = "created_at")
    private  LocalDateTime createdAt;

    @Column(name = "is_active")
    private  boolean isActive;


    public UserEntity(
                      String firstName,
                      String lastName,
                      String phoneNumber,
                      String email,
                      String password,
                      LocalDateTime createdAt,
                      boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }
}
