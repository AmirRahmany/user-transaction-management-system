package com.dev.user_transaction_management_system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "transaction_user")
@Entity
public record UserEntity(@Id int id,
                         @Column(name = "first_name") String firstName,
                         @Column(name = "last_name") String lastName,
                         @Column(name = "phone_number") String phoneNumber,
                         @Column(name = "email") String email,
                         @Column(name = "password") String password,
                         @Column(name = "created_at") LocalDateTime createdAt,
                         @Column(name = "is_active") boolean isActive) {



}
