package bootcamp.reto.powerup.model.userconsumer.utils;

import bootcamp.reto.powerup.model.applications.Applications;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder(toBuilder = true)
public class ApplicationsUserAppsResponse {
    private List<ApplicationsResponse> appsAproved;
    private UserConsumer userConsumer;
    private Applications applications;
    private BigDecimal interest_rate;
}
