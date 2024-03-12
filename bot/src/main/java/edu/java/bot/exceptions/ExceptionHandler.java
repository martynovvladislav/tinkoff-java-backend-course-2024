package edu.java.bot.exceptions;

import edu.java.bot.dtos.ApiErrorResponseDto;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    public static final String REQUEST_ERROR_DESCRIPTION = "Incorrect request parameters";
    public static final String CHAT_ERROR_DESCRIPTION = "Chat has not been found";

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> notValidArgument(MethodArgumentNotValidException e) {
        log.info(REQUEST_ERROR_DESCRIPTION);
        return new ResponseEntity<>(
            new ApiErrorResponseDto(
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

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponseDto> notReadableMessage(HttpMessageNotReadableException e) {
        log.info(REQUEST_ERROR_DESCRIPTION);
        return new ResponseEntity<>(
            new ApiErrorResponseDto(
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

    @org.springframework.web.bind.annotation.ExceptionHandler(ChatDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponseDto> wrongChatId(ChatDoesNotExistException e) {
        log.info(CHAT_ERROR_DESCRIPTION);
        return new ResponseEntity<>(
            new ApiErrorResponseDto(
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
