package uk.co.alpha60.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super("Could not find customer '" + customerId + "'.");
    }
}
