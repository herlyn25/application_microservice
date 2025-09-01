package bootcamp.reto.powerup.model.exceptions;

import bootcamp.reto.powerup.model.ConstantsApplicattions;

public class TypeLoanException extends RuntimeException {
    public TypeLoanException(String code) {
        super(String.format(ConstantsApplicattions.NO_EXISTS,code));
    }
}
