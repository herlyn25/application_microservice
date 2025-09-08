package bootcamp.reto.powerup.model.userconsumer.utils;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationsResponse {
    private BigDecimal amount;
    private Integer terms;
    private String email;
    private String name;
    private BigDecimal interest_rate;
    private String description;
}