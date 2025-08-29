package bootcamp.reto.powerup.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_type")
public class LoanTypeEntity {
    @Id private Long id;
    @Column("uniqueCode") String uniqueCode;
    @Column("name") String name;
    @Column("minimumAmount") BigDecimal minimumAmount;
    @Column("maximumAmount") BigDecimal maximumAmount;
    @Column("interestRate") BigDecimal interestRate;
    @Column("automaticValidation") Boolean automaticValidation;
}