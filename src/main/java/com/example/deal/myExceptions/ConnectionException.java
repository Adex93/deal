package com.example.deal.myExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ConnectionException extends RuntimeException {

    public ConnectionException(String message) {
        super(message);
    }
}
