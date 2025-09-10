package bootcamp.reto.powerup.consumer.exceptions;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
