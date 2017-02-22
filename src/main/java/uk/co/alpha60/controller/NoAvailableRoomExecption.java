package uk.co.alpha60.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoAvailableRoomExecption extends RuntimeException {
    public NoAvailableRoomExecption() {
        super();
    }
}
