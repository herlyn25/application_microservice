package bootcamp.reto.powerup.sqs.listener.entity;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplicationSQSResponse {
    @SerializedName("estado")
    private Long idState;

    @SerializedName("id_credit_request")
    private Long idApplication;

    @SerializedName("id_credit_request")
    private String listPays;
}
