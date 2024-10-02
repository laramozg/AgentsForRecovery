package org.example.sports.exceptions;

public class IncorrectUserCredentialsException extends RuntimeException{
    public IncorrectUserCredentialsException(String massage) {
        super(massage);
    }
}