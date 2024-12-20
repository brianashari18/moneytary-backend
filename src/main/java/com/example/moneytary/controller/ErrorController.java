package com.example.moneytary.controller;

import com.example.moneytary.dto.WebResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value()).status("Bad Request").errors(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(
                        WebResponse.<String>builder()
                                .code(exception.getStatusCode().value())
                                .status(((HttpStatus) exception.getStatusCode()).getReasonPhrase()) // Cast ke HttpStatus
                                .errors(exception.getReason())
                                .data(null)
                                .build()
                );

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WebResponse<String>> handleJsonParseError(HttpMessageNotReadableException exception) {
        Throwable rootCause = findRootCause(exception); // Cari penyebab utama
        log.info(rootCause.getMessage());
        if (rootCause instanceof DateTimeParseException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(WebResponse.<String>builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .status("Bad Request")
                            .errors("Format tanggal tidak valid. Gunakan format yyyy-MM-dd.")
                            .build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status("Bad Request")
                        .errors("Request body tidak valid.")
                        .build());
    }

    private Throwable findRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause != cause.getCause()) {
            cause = cause.getCause();
        }
        return cause;
    }
}
