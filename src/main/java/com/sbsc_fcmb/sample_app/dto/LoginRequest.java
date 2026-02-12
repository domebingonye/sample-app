package com.sbsc_fcmb.sample_app.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String password;
    private String username;
}
