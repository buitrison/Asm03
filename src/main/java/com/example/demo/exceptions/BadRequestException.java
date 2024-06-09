package com.example.demo.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
