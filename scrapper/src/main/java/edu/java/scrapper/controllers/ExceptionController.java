package edu.java.scrapper.controllers;

import edu.java.scrapper.dtos.ApiErrorResponse;
import edu.java.scrapper.exceptions.ChatDoesNotExistException;
import edu.java.scrapper.exceptions.LinkAlreadyExistException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    private final static String REQUEST_EXCEPTION_DESCRIPTION = "Incorrect request parameters";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> notValidArgument(MethodArgumentNotValidException e) {
        log.info("incorrect parameters exception");
        return new ResponseEntity<>(
                new ApiErrorResponse(
                    REQUEST_EXCEPTION_DESCRIPTION,
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    e.getClass().getSimpleName(),
                    e.getLocalizedMessage(),
                    Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                ),
            HttpStatus.BAD_REQUEST
            );
    }

    @ExceptionHandler(ChatDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> wrongChatId(ChatDoesNotExistException e) {
        log.info("chat does not exist exception");
        return new ResponseEntity<>(
            new ApiErrorResponse(
                "Chat has not been found",
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                e.getClass().getSimpleName(),
                e.getLocalizedMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList()
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LinkAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> charAlreadyExist(LinkAlreadyExistException e) {
        log.info("link already exist exception");
        return new ResponseEntity<>(
            new ApiErrorResponse(
                "link already exist",
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                e.getClass().getSimpleName(),
                e.getLocalizedMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList()
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> notReadableMessage(HttpMessageNotReadableException e) {
        log.info("Bad parameters");
        return new ResponseEntity<>(
            new ApiErrorResponse(
                REQUEST_EXCEPTION_DESCRIPTION,
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                e.getClass().getSimpleName(),
                e.getLocalizedMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList()
            ),
            HttpStatus.BAD_REQUEST
        );
    }
}
