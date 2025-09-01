package bootcamp.reto.powerup.model.loantype;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum LoanTypeEnum {

    MORTGAGE(1L, "MORT-001", "Mortgage Loan", new BigDecimal(50000), new BigDecimal("500000"),
            new BigDecimal("0.045"), false),

    PERSONAL(2L, "PERS-001", "Personal Loan", new BigDecimal("1000"), new BigDecimal("50000"),
            new BigDecimal("0.12"), true),

    VEHICLE(3L, "VEH-001", "Vehicle Loan", new BigDecimal("5000"), new BigDecimal("100000"),
            new BigDecimal("0.085"), true),

    STUDENT(4L, "STUD-001", "Student Loan", new BigDecimal("500"), new BigDecimal("40000"),
            new BigDecimal("0.05"), false);

    private final Long id;
    private final String uniqueCode;
    private final String name;
    private final BigDecimal minimumAmount;
    private final BigDecimal maximumAmount;
    private final BigDecimal interestRate;
    private final Boolean automaticValidation;

    LoanTypeEnum(Long id, String uniqueCode, String name,
             BigDecimal minimumAmount, BigDecimal maximumAmount,
             BigDecimal interestRate, Boolean automaticValidation) {
        this.id = id;
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.interestRate = interestRate;
        this.automaticValidation = automaticValidation;
    }
}
