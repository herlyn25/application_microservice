package bootcamp.reto.powerup.model.states;

import lombok.Getter;

public enum StatesEnum {
    PENDIENTE(1L,"PPV", "Pendiente por verificación", "Espera aprobacion del gerente"),
    ENPROCESO(2L,"EN", "En proceso", "Asesor pedirá la documentación"),
    APROBADO(3L,"APROB", "Aprobado", "Cliente entrego toda la documentacion"),
    DESEMBOLSADO(4L,"DSB", "Desembolsado a cliente", "Ya el cliente recibio el dinero");

    @Getter private Long id;
    @Getter private String code;
    @Getter private String name;
    @Getter private String description;

    StatesEnum(Long id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }
}
