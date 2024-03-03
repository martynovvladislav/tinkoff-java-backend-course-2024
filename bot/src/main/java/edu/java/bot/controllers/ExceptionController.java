package edu.java.bot.controllers;

import edu.java.bot.dtos.ApiErrorResponse;
import edu.java.bot.exceptions.ChatDoesNotExistException;
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
    public static final String REQUEST_ERROR_DESCRIPTION = "Incorrect request parameters";
    public static final String CHAT_ERROR_DESCRIPTION = "Chat has not been found";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> notValidArgument(MethodArgumentNotValidException e) {
        log.info(REQUEST_ERROR_DESCRIPTION);
        return new ResponseEntity<>(
            new ApiErrorResponse(
                REQUEST_ERROR_DESCRIPTION,
                e.getStatusCode().toString(),
                e.getClass().getSimpleName(),
                e.getDetailMessageCode(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList()
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> notReadableMessage(HttpMessageNotReadableException e) {
        log.info(REQUEST_ERROR_DESCRIPTION);
        return new ResponseEntity<>(
            new ApiErrorResponse(
                REQUEST_ERROR_DESCRIPTION,
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
        log.info(CHAT_ERROR_DESCRIPTION);
        return new ResponseEntity<>(
            new ApiErrorResponse(
                CHAT_ERROR_DESCRIPTION,
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
