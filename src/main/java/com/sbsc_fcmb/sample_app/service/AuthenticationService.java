package com.sbsc_fcmb.sample_app.service;

import com.sbsc_fcmb.sample_app.config.PropsReader;
import com.sbsc_fcmb.sample_app.dto.LoginRequest;
import com.sbsc_fcmb.sample_app.dto.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final SystemUserDaoService systemUserDaoService;
    private final PropsReader propsReader;
    private final AuthenticationProvider userAuthenticationProvider;

    public LoginResponse processLogin(HttpServletRequest request, LoginRequest login) {

        String username = login.getUsername();
        String password = login.getPassword();
        String remoteAddr = request.getRemoteAddr();

        log.info("Trying to login " + username + " from " + remoteAddr);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        token.setDetails(new WebAuthenticationDetails(request));
        log.info("Token " + token);
        try {
            Authentication auth = userAuthenticationProvider.authenticate(token);

            log.info("Authorities for " + username + ": " + auth.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(",")));

            SecurityContextHolder.getContext().setAuthentication(auth);

            var user = systemUserDaoService.findByUsername(username);

            log.info("User " + user.getUsername() + ": login successful");
            var userDetails = (UserDetails) auth.getPrincipal();

            String jwt = generateJwtToken(auth);
            return LoginResponse.builder()
                    .token("Bearer " + jwt)
                    .email(user.getEmail())
                    .code(user.getCode())
                    .firstName(user.getFirstName())
                    .surname(user.getSurname())
                    .userRoleType(user.getUserRoleType())
                    .active(user.isActive())
                    .userDetails(userDetails)
                    .build();
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " wrong login details");
        }
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis() + (propsReader.getAccessTokenValidityInSeconds()) * 1000)))
                .signWith(SignatureAlgorithm.HS512, propsReader.getClientSecret())
                .compact();
    }
}
