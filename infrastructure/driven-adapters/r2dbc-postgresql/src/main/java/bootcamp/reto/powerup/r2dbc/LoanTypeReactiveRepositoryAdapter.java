package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.exceptions.ResourceNotFoundException;
import bootcamp.reto.powerup.model.exceptions.TypeLoanException;
import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.loantype.LoanTypeEnum;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import bootcamp.reto.powerup.r2dbc.entities.LoanTypeEntity;
import bootcamp.reto.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        Long,
        LoanTypeReactiveRepository
> implements LoanTypeRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, LoanType.class/* change for domain model */));
    }

    @Override
    public Mono<LoanType> findLoanByCode(String code) {
        return findLoanTypeFromEnum(code);
    }

    private Mono<LoanType> findLoanTypeFromEnum(String code){

        for(LoanTypeEnum loanTypeEnum: LoanTypeEnum.values()){
            if (loanTypeEnum.getUniqueCode().equals(code)){
                LoanType loanType = new LoanType(
                        loanTypeEnum.getId(),
                        loanTypeEnum.getUniqueCode(),
                        loanTypeEnum.name(),
                        loanTypeEnum.getMinimumAmount(),
                        loanTypeEnum.getMaximumAmount(),
                        loanTypeEnum.getInterestRate(),
                        loanTypeEnum.getAutomaticValidation()
                );
                return Mono.just(loanType);
            }
        }
        return Mono.error(new ResourceNotFoundException(ConstantsApps.TYPE_LOAN_NO_EXIST));
    }
}
