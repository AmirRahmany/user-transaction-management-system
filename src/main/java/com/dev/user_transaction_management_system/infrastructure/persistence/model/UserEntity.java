package com.dev.user_transaction_management_system.infrastructure.persistence.model;

import com.dev.user_transaction_management_system.domain.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Table(name = "user_account")
@Entity
@NoArgsConstructor
@Data
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;


    public UserEntity(
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            String password,
            LocalDateTime createdAt,
            UserStatus userStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.userStatus = userStatus;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList() ;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
