package bootcamp.reto.powerup.exceptions;

import bootcamp.reto.powerup.model.exceptions.ApplicationValidationException;
import bootcamp.reto.powerup.model.exceptions.TypeLoanException;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;
import java.util.LinkedHashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationValidationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUserValidation(ApplicationValidationException ex) {
        Map<String, Object> body = createErrorResponse(
                "Validation Error",
                ex.getMessage(),
                ex.getErrors()
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(TypeLoanException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleEmailAlreadyUsed(TypeLoanException ex) {
        Map<String, Object> body = createErrorResponse(
                "Loan Type",
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = createErrorResponse(
                "Invalid Arguments",
                ex.getMessage() != null ? ex.getMessage() : "Invalid request parameters",
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = createErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred",
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
        Map<String, Object> body = createErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred",
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Map<String, Object> createErrorResponse(String error, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("error", error);
        body.put("message", message);
        if (details != null) {
            body.put("details", details);
        }
        return body;
    }
}