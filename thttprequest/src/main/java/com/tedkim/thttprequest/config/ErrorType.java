package com.tedkim.thttprequest.config;

/**
 * Class collection of error type when API Requests
 * Created by Ted
 */
public class ErrorType {

    public class COMMON {
        static final String COMMON = "common.";

        public static final String INVALID_TOKEN = COMMON + "invalid_token";
        public static final String EXPIRED_TOKEN = COMMON + "expired_token";
        public static final String NOT_EXISTS_TOKEN = COMMON + "not_exists_token";
    }

    public class AUTH {
        static final String AUTH = "auth.";
        public static final String EXPIRED_TOKEN = AUTH + "expired_token";
    }

    public class ANDROID {
        public static final String NOT_SIGN_UP = "not_sign_up";
    }

    public class USER {
        public static final String INVALID_SWIFT_CODE = "user.invalid_swift_code";
    }
}