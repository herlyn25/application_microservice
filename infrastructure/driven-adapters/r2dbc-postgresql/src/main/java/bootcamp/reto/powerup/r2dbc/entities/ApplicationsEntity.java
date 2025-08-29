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
@Table(name = "credit_requests")
public class ApplicationsEntity {
    @Id private Long id;
    @Column("amount") private BigDecimal amount;
    @Column("term") private Integer term;
    @Column("email") private String email;
    @Column("term") private Integer terms;
    @Column("state") private StatesEntity state;
    @Column("loan_type") private LoanTypeEntity loanType;
    @Column("doccument_id") private String doccumentId;
}
