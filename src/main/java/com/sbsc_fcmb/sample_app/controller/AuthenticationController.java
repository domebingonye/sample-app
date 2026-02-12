package com.sbsc_fcmb.sample_app.controller;

import com.sbsc_fcmb.sample_app.dto.LoginRequest;
import com.sbsc_fcmb.sample_app.dto.LoginResponse;
import com.sbsc_fcmb.sample_app.dto.SystemUser;
import com.sbsc_fcmb.sample_app.enums.UserRoleType;
import com.sbsc_fcmb.sample_app.service.AuthenticationService;
import com.sbsc_fcmb.sample_app.service.SystemUserDaoService;
import com.sbsc_fcmb.sample_app.service.SystemUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/authentication")
public class AuthenticationController {
    private final SystemUserDaoService systemUserDaoService;
    private final SystemUserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<SystemUser> create(@Valid @RequestBody SystemUser user) {
        return ResponseEntity.ok().body(userService.create(user));
    }

    @PutMapping("/role/{code}/{roleType}")
    @PreAuthorize("hasAnyAuthority('" + UserRoleType.Code.ROLE_ADMIN + "')")
    public ResponseEntity<String> updateRole(@PathVariable final String code, @Valid @PathVariable UserRoleType roleType) {
        return ResponseEntity.ok().body(systemUserDaoService.updateRole(code, roleType));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletRequest request, @Valid @RequestBody LoginRequest login) {
        return ResponseEntity.ok().body(authenticationService.processLogin(request, login));
    }
}
