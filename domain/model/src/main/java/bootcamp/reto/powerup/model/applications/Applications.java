package bootcamp.reto.powerup.model.applications;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Applications {
    private Long id;
    private BigDecimal amount;
    private Integer terms;
    private String email;
    private String states;
    private String loanType;
    private String documentId;
    private LocalDate created;
}