package uk.co.alpha60.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidBookingDatesException extends RuntimeException {
    public InvalidBookingDatesException(String message) {
        super(message);
    }
}
