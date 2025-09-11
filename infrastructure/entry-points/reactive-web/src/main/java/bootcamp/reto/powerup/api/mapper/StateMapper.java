package bootcamp.reto.powerup.api.mapper;

import bootcamp.reto.powerup.api.dto.StateDTO;
import bootcamp.reto.powerup.model.states.States;
import org.springframework.stereotype.Component;

@Component
public class StateMapper {
    public States dtoToState(StateDTO stateDTO) {
        States states = new States(
                null,
                stateDTO.name(),
                stateDTO.description()
        );
        return states;
    }
}
