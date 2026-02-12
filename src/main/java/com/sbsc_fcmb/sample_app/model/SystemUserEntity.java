package com.sbsc_fcmb.sample_app.model;

import com.sbsc_fcmb.sample_app.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Accessors(chain = true)
@Data
@NoArgsConstructor
@Table(name = "SYS_USERS")
public class SystemUserEntity extends AuditModel{
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Lob
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "USER_CODE", nullable = false)
    private String code;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;

    @Column(name = "ACTIVE", columnDefinition="bit default 1", nullable = false)
    private boolean active;
}
