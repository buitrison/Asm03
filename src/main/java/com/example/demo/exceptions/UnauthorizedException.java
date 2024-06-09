package com.example.demo.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
