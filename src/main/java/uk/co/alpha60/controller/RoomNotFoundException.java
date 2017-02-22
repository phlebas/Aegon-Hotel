package uk.co.alpha60.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long roomId) {
        super("Could not find room '" + roomId + "'.");
    }
}
