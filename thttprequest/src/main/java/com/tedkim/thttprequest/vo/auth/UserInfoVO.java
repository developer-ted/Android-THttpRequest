package com.tedkim.thttprequest.vo.auth;

/**
 * Created by Ted
 */
public class UserInfoVO {

    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String profile_img;
    private String accessToken;
    private String type;
    private String verified_email;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getType() {
        return type;
    }

    public String getVerified_email() {
        return verified_email;
    }
}
