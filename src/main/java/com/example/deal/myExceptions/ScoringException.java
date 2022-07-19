package com.example.deal.myExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
public class ScoringException extends RuntimeException{

    public ScoringException(String message) {
        super(message);
    }
}
