package com.sbsc_fcmb.sample_app.service;

import com.sbsc_fcmb.sample_app.dto.SystemUser;
import com.sbsc_fcmb.sample_app.enums.ResponseCodes;
import com.sbsc_fcmb.sample_app.enums.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemUserService implements UserDetailsService {
    private final SystemUserDaoService systemUserDaoService;
    private final PasswordEncoder passwordEncoder;

    public SystemUser create(SystemUser systemUser) {
        if (systemUserDaoService.findByUsername(systemUser.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The username " + systemUser.getUsername() + " exists");
        }

        if (systemUserDaoService.findByEmail(systemUser.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The email " + systemUser.getEmail() + " exists");
        }
        String generatedPassword = !StringUtils.hasText(systemUser.getPassword()) ? RandomStringUtils.randomAlphanumeric(10) : systemUser.getPassword();

        systemUser.setPassword(passwordEncoder.encode(generatedPassword));
        systemUser.setCode(systemUserDaoService.userCode(systemUser.getUsername()));
        systemUser.setUsername(systemUser.getUsername());
        systemUser.setSurname(systemUser.getSurname());
        systemUser.setUserRoleType(!ObjectUtils.isEmpty(systemUser.getUserRoleType()) ? systemUser.getUserRoleType() : UserRoleType.USER);

        var result = systemUserDaoService.save(systemUser);
        log.info("Result" + result);
        systemUser.setId(result.getId());
        systemUser.setCode(result.getCode());
        return systemUser;
    }

    public SystemUser changePassword(String code, String password) {
        SystemUser user = systemUserDaoService.findByCode(code);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user  with code " + code + " does not exists");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRoleType(!ObjectUtils.isEmpty(user.getUserRoleType()) ? user.getUserRoleType() : UserRoleType.USER);
        return systemUserDaoService.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String userName) {
        SystemUser systemUser = Optional.ofNullable(systemUserDaoService.findByUsername(userName)).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("User: %s not found", userName)));
        if (systemUser.getUsername().equals(userName)) {
            GrantedAuthority authority = new SimpleGrantedAuthority(systemUser.getUserRoleType().name());

            return new User(systemUser.getUsername(), systemUser.getPassword(), Collections.singletonList(authority));
        } else throw new UsernameNotFoundException("User with " + userName + " not found");
    }
}
