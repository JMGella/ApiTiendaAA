package org.example.apitiendaaa.exception;

import java.util.Map;

public class ErrorResponse {

    private int errorcode;
    private String message;
    private Map<String, String> errorMessages;


    private ErrorResponse(int code, String message) {
        this.errorcode = code;
        this.message = message;
    }


    private ErrorResponse(Map<String, String> errorMessages) {
        this.errorcode = 400;
        this.message = "Bad Request";
        this.errorMessages = errorMessages;
    }

    public static ErrorResponse generalError(int code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse validationError(int i, String validationError, Map<String, String> errors) {
        return new ErrorResponse(errors);
    }

}