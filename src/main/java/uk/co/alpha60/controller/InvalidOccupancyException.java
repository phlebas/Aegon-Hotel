package uk.co.alpha60.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidOccupancyException extends RuntimeException {
    public InvalidOccupancyException(Integer noOfGuests) {
        super("Number of guests " + noOfGuests + " exceeds maximum room occupancy.");
    }
}
