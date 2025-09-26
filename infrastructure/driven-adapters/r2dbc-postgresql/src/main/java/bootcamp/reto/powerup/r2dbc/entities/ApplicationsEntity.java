package bootcamp.reto.powerup.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credit_requests")
public class ApplicationsEntity {
    @Id private Long id;
    @Column("amount") private BigDecimal amount;
    @Column("email") private String email;
    @Column("terms") private Integer terms;
    @Column("states") private String states;
    @Column("loan_type") private String loanType;
    @Column("document_id") private String documentId;
    @Column("created")  private LocalDate created;
}
