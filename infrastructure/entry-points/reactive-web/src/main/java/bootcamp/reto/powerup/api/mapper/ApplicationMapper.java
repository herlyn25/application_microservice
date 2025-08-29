package bootcamp.reto.powerup.api.mapper;

import bootcamp.reto.powerup.api.dto.ApplicationsDTO;
import bootcamp.reto.powerup.model.applications.Applications;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {
    StateMapper stateMapper = new StateMapper();
    LoanTypeMapper loanTypeMapper = new LoanTypeMapper();

    public Applications dtoToApplications(ApplicationsDTO applicationsDTO) {
        Applications applications = new Applications(
                null,
                applicationsDTO.amount(),
                applicationsDTO.terms(),
                applicationsDTO.email(),
                stateMapper.dtoToState(applicationsDTO.state()),
                loanTypeMapper.dtoToLoanType(applicationsDTO.loanType()),
                applicationsDTO.documentId()
        );
        return applications;
    }
}
