package com.sbsc_fcmb.sample_app.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedHashMap;
import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.empty();
        }

        LinkedHashMap<String, Object> userDetails = (LinkedHashMap<String, Object>) authentication.getPrincipal();
        return Optional.of(userDetails).map(m -> String.valueOf(m.get("username")));
    }
}
