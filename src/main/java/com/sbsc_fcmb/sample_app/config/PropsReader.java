package com.sbsc_fcmb.sample_app.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class PropsReader {
    @Value("${client.secret}")
    private String clientSecret;

    @Value("${jwt.accessTokenValiditySeconds}")
    private long accessTokenValidityInSeconds;
}
