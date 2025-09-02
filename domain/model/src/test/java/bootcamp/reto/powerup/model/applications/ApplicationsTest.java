package bootcamp.reto.powerup.model.applications;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationsTest {

    @Test
    @DisplayName("Show create application with all fields using builder")
    void showCreateApplicationWithAllFieldsUsingBuilder() {
        Applications app = Applications.builder()
                .id(1L)
                .amount(new BigDecimal("50000"))
                .terms(24)
                .email("user@email.com")
                .states("PENDIENTE")
                .loanType("AUTO")
                .documentId("13572468")
                .build();
        assertEquals(1L, app.getId());
        assertEquals(app.getAmount(),new BigDecimal("50000"));
        assertEquals(app.getTerms(),24);
        assertEquals(app.getEmail(),"user@email.com");
        assertEquals(app.getStates(),"PENDIENTE");
        assertEquals(app.getLoanType(),"AUTO");
        assertEquals(app.getDocumentId(),"13572468");
    }
}