package com.newlight77.kata.survey.Exceptions;

public class SendMailException extends Exception {

    public SendMailException() {
    }

    public SendMailException(String message, Exception e) {
            super(message, e);
    }

}
