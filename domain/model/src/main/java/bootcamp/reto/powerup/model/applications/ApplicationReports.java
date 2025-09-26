package bootcamp.reto.powerup.model.applications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ApplicationReports {
    private Integer appsApproved;
    private BigDecimal amountAccum;
}
