package com.mobilemessagesgateway.configuration;

import java.io.IOException;
import java.util.Objects;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Custom exception handler to be able to use javax validation annotation and still return a 400. As ConstraintViolationException is not handled
     * by springs DefaultHandlerExceptionResolver the validation error would return a 500 without this custom exception handler.
     *
     * @param ex      message body
     * @param request phone number
     */
    @ExceptionHandler({ConstraintViolationException.class})
    protected void handleConstraintViolation(RuntimeException ex, ServletWebRequest request) throws IOException {
        Objects.requireNonNull(request.getResponse()).sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
