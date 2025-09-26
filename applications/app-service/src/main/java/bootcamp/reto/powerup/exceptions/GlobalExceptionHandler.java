package bootcamp.reto.powerup.exceptions;

import bootcamp.reto.powerup.consumer.exceptions.JwtException;
import bootcamp.reto.powerup.model.exceptions.ApplicationValidationException;
import bootcamp.reto.powerup.model.exceptions.ResourceNotFoundException;
import bootcamp.reto.powerup.model.exceptions.TypeLoanException;
import bootcamp.reto.powerup.model.exceptions.ValidateStatesException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;
import java.util.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidateStatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationStates(ValidateStatesException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsMessageToException.ERROR_VALIDATION,
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleResourceNotFoundValidation(ResourceNotFoundException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsMessageToException.ERROR_VALIDATION,
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.NOT_FOUND));
    }
    @ExceptionHandler({ServerWebInputException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(ServerWebInputException ex) {
        String message = ConstantsMessageToException.BAD_JSON_FORMAT;

        Throwable cause = ex.getCause();
        if (cause instanceof JsonParseException || cause instanceof JsonMappingException) {
            message = cause.getMessage();
        }
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Bad Request");
        body.put("message", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
    @ExceptionHandler(ApplicationValidationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUserValidation(ApplicationValidationException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsMessageToException.ERROR_VALIDATION,
                ex.getMessage(),
                ex.getErrors()
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(TypeLoanException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleEmailAlreadyUsed(TypeLoanException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsMessageToException.LOAN_TYPE_NOT_FOUND,
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = createErrorResponse(
               ConstantsMessageToException.INVALID_ARGUMENTS,
                ex.getMessage() != null ? ex.getMessage() : ConstantsMessageToException.INVALID_REQUEST_PARAMETERS,
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(JwtException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleAuthorizationException(JwtException jwtEx) {
        Map<String, Object> body = createErrorResponse(
                ConstantsMessageToException.AUTHORIZATION_ERRORS,
                jwtEx.getMessage() ,
                null
        );
        return Mono.just(new ResponseEntity<>(body, jwtEx.getHttpStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: ", ex);
        Map<String, Object> body = createErrorResponse(
                ConstantsMessageToException.INTERNAL_SERVER_ERROR,
                ConstantsMessageToException.AN_UNEXPECTED_ERROR,
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
        Map<String, Object> body = createErrorResponse(
                ex.toString(),
                ConstantsMessageToException.AN_UNEXPECTED_ERROR,
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Map<String, Object> createErrorResponse(String error, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ConstantsMessageToException.TIMESTAMP, Instant.now().toString());
        body.put(ConstantsMessageToException.ERROR, error);
        body.put(ConstantsMessageToException.MESSAGE, message);
        if (details != null) {
            body.put(ConstantsMessageToException.DETAILS, details);
        }
        return body;
    }
}