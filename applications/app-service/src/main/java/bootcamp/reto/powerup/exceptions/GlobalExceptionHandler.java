package bootcamp.reto.powerup.exceptions;

import bootcamp.reto.powerup.consumer.exceptions.JwtException;
import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.exceptions.ApplicationValidationException;
import bootcamp.reto.powerup.model.exceptions.TypeLoanException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;
import java.util.*;

import static bootcamp.reto.powerup.model.ConstantsApps.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ServerWebInputException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(ServerWebInputException ex) {

        String message = "Malformed JSON request";

        // Si viene de Jackson, podemos obtener detalle
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

    @ExceptionHandler(JwtException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleAuthorizationException(JwtException jwtEx) {
        Map<String, Object> body = createErrorResponse(
                "prblemas de autorizacion",
                jwtEx.getMessage() ,
                null
        );
        return Mono.just(new ResponseEntity<>(body, jwtEx.getHttpStatus()));

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
                ex.toString(),
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