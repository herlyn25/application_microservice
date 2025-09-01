package bootcamp.reto.powerup.model.exceptions;

import bootcamp.reto.powerup.model.ConstantsApps;

public class TypeLoanException extends RuntimeException {
    public TypeLoanException(String code) {
        super(String.format(ConstantsApps.TYPE_LOAN_NO_EXIST));
    }
}
