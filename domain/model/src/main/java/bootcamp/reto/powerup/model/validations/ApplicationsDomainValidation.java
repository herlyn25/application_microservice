package bootcamp.reto.powerup.model.validations;

import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.exceptions.ApplicationValidationException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Pattern;

public class ApplicationsDomainValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(ConstantsApps.PATTERN_EMAIL);
    private static final Pattern DOCUMENT_ID_PATTERN = Pattern.compile(ConstantsApps.PATTERN_DOCUMENT_ID);


    public static Mono<Applications> validateApplications(Applications app) {
        List<String> errors = new ArrayList<>();
        if (app.getEmail() == null || app.getEmail().isEmpty()) {
            errors.add(ConstantsApps.REQUIRED_EMAIL);
        } else if (!EMAIL_PATTERN.matcher(app.getEmail()).matches()) {
            errors.add(ConstantsApps.INVALID_EMAIL);
        }

        if(app.getDocumentId() == null || app.getDocumentId().isEmpty()) {
            errors.add(ConstantsApps.REQUIRED_DOCUMENT);
        } else if(!DOCUMENT_ID_PATTERN.matcher(app.getDocumentId()).matches()) {
            errors.add(ConstantsApps.INVALID_DOCUMENT_ID);
        }

        if(app.getAmount()==null) {
            errors.add(ConstantsApps.REQUIRED_AMOUNT);
        }

        if(app.getTerms()==null) {
            errors.add(ConstantsApps.REQUIRED_TERMS);
        }

        if(app.getLoanType()==null) {
            errors.add(ConstantsApps.REQUIRED_TYPE_LOAN);
        }

        if (!errors.isEmpty()) {
            return Mono.error(new ApplicationValidationException(errors));
        }
        return Mono.just(app);
    }
}