package com.trainbooking.cloudbees.exception;

public class NoSeatsAvailableException extends RuntimeException {
    public NoSeatsAvailableException(String message) {
        super(message);
    }
}

