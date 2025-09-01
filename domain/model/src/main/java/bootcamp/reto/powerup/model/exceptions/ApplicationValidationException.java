package bootcamp.reto.powerup.model.exceptions;

import java.util.List;

public class ApplicationValidationException extends RuntimeException {
    private final List<String> errors ;
    public ApplicationValidationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
