package com.sbsc_fcmb.sample_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sbsc_fcmb.sample_app.enums.UserRoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
public class SystemUser {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private Long id;
    @NotBlank(message = "Email is required.")
    private String email;
    @Schema(name = "code", requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private String code;
    private String password;
    private String surname;
    private String firstName;
    @NotBlank(message = "Username is required.")
    private String username;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private UserRoleType userRoleType = UserRoleType.USER;
    private boolean active = true;
    private Map<String, String> additionalInfo;
}
