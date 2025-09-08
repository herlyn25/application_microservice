package bootcamp.reto.powerup.model.userconsumer.utils;
import lombok.*;
//import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserConsumer {
    private String fullname;
    private BigDecimal salary;
}
