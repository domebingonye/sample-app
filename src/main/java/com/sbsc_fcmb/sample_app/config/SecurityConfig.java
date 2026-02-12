package com.sbsc_fcmb.sample_app.config;

import com.sbsc_fcmb.sample_app.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("DATABASE")
@RequiredArgsConstructor
public class SecurityConfig {

    private final SystemUserService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}

