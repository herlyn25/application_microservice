package bootcamp.reto.powerup.model.userconsumer;

import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class UserConsumerFull {
    private BigDecimal amount;
    private Integer terms;
    private String email;
    private String name;
    private BigDecimal interest_rate;
    private String description;
    private String fullname;
    private BigDecimal salary;

    public UserConsumerFull(ApplicationsResponse response, UserConsumer userConsumer) {
        this.amount = response.getAmount();
        this.terms = response.getTerms();
        this.email = response.getEmail();
        this.name = response.getName();
        this.interest_rate = response.getInterest_rate();
        this.description = response.getDescription();
        this.fullname = userConsumer.getFullname();
        this.salary = userConsumer.getSalary();
    }
}
