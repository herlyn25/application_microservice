package bootcamp.reto.powerup.model.states;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class States {
    private Integer id;
    private String uniqueCode;
    private String name;
    private String description;

}
