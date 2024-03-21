package edu.java.scrapper.exceptions;

import edu.java.scrapper.dtos.ApiErrorResponseDto;
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
public class RestExceptionHandler {
    private final static String REQUEST_EXCEPTION_DESCRIPTION = "Incorrect request parameters";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> notValidArgument(MethodArgumentNotValidException e) {
        log.info("incorrect parameters exception");
        return new ResponseEntity<>(
                new ApiErrorResponseDto(
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
    public ResponseEntity<ApiErrorResponseDto> wrongChatId(ChatDoesNotExistException e) {
        log.info("chat does not exist exception");
        return new ResponseEntity<>(
            new ApiErrorResponseDto(
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
    public ResponseEntity<ApiErrorResponseDto> charAlreadyExist(LinkAlreadyExistException e) {
        log.info("link already exist exception");
        return new ResponseEntity<>(
            new ApiErrorResponseDto(
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
    public ResponseEntity<ApiErrorResponseDto> notReadableMessage(HttpMessageNotReadableException e) {
        log.info("Bad parameters");
        return new ResponseEntity<>(
            new ApiErrorResponseDto(
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
