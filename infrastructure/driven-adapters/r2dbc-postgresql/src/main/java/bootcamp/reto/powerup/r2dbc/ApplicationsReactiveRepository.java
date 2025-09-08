package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import bootcamp.reto.powerup.r2dbc.entities.ApplicationsEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

// TODO: This file is just an example, you should delete or modify it
public interface ApplicationsReactiveRepository extends ReactiveCrudRepository<ApplicationsEntity, Long>, ReactiveQueryByExampleExecutor<ApplicationsEntity> {
    @Query("""
        SELECT cr.amount, cr.terms, cr.email,
               lty.name, lty.interest_rate,
               st.description        
        FROM credit_requests cr         
        JOIN loan_type lty ON cr.loan_type = lty.unique_code
        JOIN states st ON cr.states = st.name
        WHERE cr.states = 'PPV'
        LIMIT :limit OFFSET :offset
        """)
    Flux<ApplicationsResponse> findAppsByPage(int limit, int offset);

    @Query("""
        SELECT cr.amount, cr.terms, cr.email,
               lty.name, lty.interest_rate,
               st.description        
        FROM credit_requests cr         
        JOIN loan_type lty ON cr.loan_type = lty.unique_code
        JOIN states st ON cr.states = st.name
        WHERE cr.states = 'PPV'
        """)
    Flux<ApplicationsResponse> findAllAppsNew();

    @Query("""
        SELECT sum(cr.amount) as amountAprob FROM credit_requests cr
        JOIN states st ON cr.states = st.name
        WHERE cr.states = 'APROB'
    """)
    Mono<BigDecimal> totalAmountApprobation();
}