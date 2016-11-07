package com.tedkim.thttprequest.vo.auth;

/**
 * Created by Ted
 */
public class AuthTokenVO {

    private String uid;
    private String tk_id;
    private String rf_id;
    private String cl_id;
    private String sid;
    private long expires;

    public String getUid() {
        return uid;
    }

    public String getTk_id() {
        return tk_id;
    }

    public String getRf_id() {
        return rf_id;
    }

    public String getCl_id() {
        return cl_id;
    }

    public String getSid() {
        return sid;
    }

    public long getExpires() {
        return expires;
    }
}
