package com.tedkim.thttprequest.vo.auth;

/**
 * Created by Ted
 */
public class VerificationVO {

    private String email;
    private String passwd;
    private String accessToken;
    private String refresh_token;
    private String sid;

    public VerificationVO(String accessToken, String sid) {
        this.email = null;
        this.passwd = null;
        this.accessToken = accessToken;
        this.refresh_token = null;
        this.sid = sid;
    }

    public VerificationVO(String email, String passwd, String sid) {
        this.email = email;
        this.passwd = passwd;
        this.accessToken = null;
        this.refresh_token = null;
        this.sid = sid;
    }

    public VerificationVO(boolean isRefresh, String refresh_token, String sid) {
        this.email = null;
        this.passwd = null;
        this.accessToken = null;
        this.refresh_token = refresh_token;
        this.sid = sid;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getSid() {
        return sid;
    }
}
