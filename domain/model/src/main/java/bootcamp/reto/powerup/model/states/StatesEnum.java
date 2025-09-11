package bootcamp.reto.powerup.model.states;

import lombok.Getter;

@Getter
public enum StatesEnum {
    PENDIENTE(1L,"PPV", "Pendiente"),
    ENPROCESO(2L,"APROB", "Aprobado"),
    RECHAZADO(3L,"RCHZ", "Rechazado");

    private final Long  id;
    private final String name;
    private final String description;

    StatesEnum(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
