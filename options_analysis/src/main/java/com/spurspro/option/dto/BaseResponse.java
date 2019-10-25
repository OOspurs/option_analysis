package com.spurspro.option.dto;

import java.io.Serializable;

public abstract class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private Boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
