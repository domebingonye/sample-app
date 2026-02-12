package com.sbsc_fcmb.sample_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sbsc_fcmb.sample_app.enums.UserRoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String email;
    private String token;
    private String code;
    private String surname;
    private String firstName;
    private String username;
    private UserRoleType userRoleType;
    private Boolean active;
    private Object userDetails;
}
