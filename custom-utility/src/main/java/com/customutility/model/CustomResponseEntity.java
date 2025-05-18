package com.customutility.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CustomResponseEntity<T> {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int errorCode;
    private boolean success;
    private String message;
    private T data;

    public CustomResponseEntity(T data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    public CustomResponseEntity(String message) {
        this.success = true;
        this.message = message;
    }

    public CustomResponseEntity(int errorCode, String message) {
        this.errorCode = errorCode;
        this.success = false;
        this.message = message;
    }

    public CustomResponseEntity(int errorCode, String message, T data) {
        this.errorCode = errorCode;
        this.success = false;
        this.message = message;
        this.data = data;
    }

    // Static method to create an ApiResponse for exceptions
    public static <T> CustomResponseEntity<T> errorResponse(Exception exception) {
        return new CustomResponseEntity<>(1, "An error occurred: " + exception.getMessage());
    }

    public static <T> CustomResponseEntity<T> error(String error) {
        CustomResponseEntity<T> response = new CustomResponseEntity<>();
        response.setMessage(error);
        response.setErrorCode(1000);
        response.setSuccess(false);
        return response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public CustomResponseEntity(int errorCode, boolean success, String message, T data) {
        this.errorCode = errorCode;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public CustomResponseEntity() {
    }
}

