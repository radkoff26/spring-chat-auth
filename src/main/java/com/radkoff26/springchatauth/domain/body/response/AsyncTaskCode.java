package com.radkoff26.springchatauth.domain.body.response;

public enum AsyncTaskCode {
    SEND_EMAIL_CODE("SEND_EMAIL_CODE"),
    SEND_EMAIL_SUBMISSION("SEND_EMAIL_SUBMISSION");
    private final String code;

    AsyncTaskCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
