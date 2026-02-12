package com.sbsc_fcmb.sample_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserRoleType {
    USER(Code.ROLE_USER),
    SUPER_USER(Code.ROLE_SUPER_USER),
    ADMIN(Code.ROLE_ADMIN),
    SUPER_ADMIN(Code.ROLE_SUPER_ADMIN),
    SYSTEM_USER(Code.SYSTEM_USER);

    @Getter
    private final String userType;

    public static class Code {
        public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
        public static final String ROLE_ADMIN = "ADMIN";
        public static final String ROLE_USER = "USER";
        public static final String ROLE_SUPER_USER = "SUPER_USER";
        public static final String SYSTEM_USER = "SYSTEM";
    }
}
