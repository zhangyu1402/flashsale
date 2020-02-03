package com.zhangyu.seckill.dto;

import java.io.Serializable;

public class Response<T> implements Serializable {

    private boolean success;

    private T body;

    private String errorMessage;

    public Response(Boolean success, T body) {
        this.success = success;
        this.body = body;
    }


    public Response(boolean success, String error) {
        this.success = success;
        this.errorMessage = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getBody() {
        return body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
