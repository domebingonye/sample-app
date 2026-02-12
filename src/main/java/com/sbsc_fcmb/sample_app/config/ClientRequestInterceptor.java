package com.sbsc_fcmb.sample_app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class ClientRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        return execution.execute(request, body);
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.info("Request: {} {}", request.getMethod(), request.getURI());
        if (body != null && body.length > 0) {
            log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
        }
    }
}
