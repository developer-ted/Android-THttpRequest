package com.tedkim.thttprequest.vo;

/**
 * Created by Ted
 */
public class ErrorVO {

    private String code;
    private String status;
    private String message;

    public ErrorVO() {}

    public ErrorVO(String code) {
        this.code = code;
        status = null;
        message = null;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
