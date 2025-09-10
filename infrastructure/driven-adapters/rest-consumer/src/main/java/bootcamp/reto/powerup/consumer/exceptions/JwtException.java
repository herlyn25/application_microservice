package bootcamp.reto.powerup.consumer.exceptions;

import org.springframework.http.HttpStatus;

public class JwtException extends RuntimeException {
    private HttpStatus httpStatus;
    public JwtException(HttpStatus httpStatus,String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
