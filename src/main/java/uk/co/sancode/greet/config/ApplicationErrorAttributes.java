package uk.co.sancode.greet.config;

import org.springframework.web.context.request.ServletWebRequest;
import uk.co.sancode.greet.model.ErrorResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.boot.web.error.ErrorAttributeOptions.defaults;

public class ApplicationErrorAttributes extends DefaultErrorAttributes {
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String ERRORS = "errors";
    private static final String TIMESTAMP = "timestamp";

    private final ErrorAttributeOptions errorAttributeOptions =
            defaults().including(Include.MESSAGE, Include.BINDING_ERRORS);

    public ErrorResponse getErrorResponse(WebRequest request) {
        var errorAttributes = getErrorAttributes(request, errorAttributeOptions);

        var status = getAttribute(errorAttributes, STATUS, Integer.class);
        var error = getAttribute(errorAttributes, ERROR, String.class);
        var message = getAttribute(errorAttributes, MESSAGE, String.class);
        var errors = getFieldErrors(getAttribute(errorAttributes, ERRORS, List.class));
        var path = ((ServletWebRequest)request).getRequest().getRequestURI();
        var date = getAttribute(errorAttributes, TIMESTAMP, Date.class);
        var timestamp = date == null ? null : date.toInstant().atOffset(ZoneOffset.UTC);
        return new ErrorResponse(status, error, message, errors, path, timestamp);
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(Map<String, Object> errorAttributes, String key, Class<T> type) {
        return (T) Optional.ofNullable(errorAttributes).map(attributes -> attributes.get(key)).filter(type::isInstance).orElse(null);
    }

    private List<String> getFieldErrors(List<?> errors) {
        if (errors == null || errors.isEmpty()) {
            return null; // This is to make the json serialization ignore the validation error field on ErrorResponse. It is configured in JacksonConfiguration class to only include non-null fields in json serialization.
        }

        return errors.stream()
                .map(error -> {
                            if (error instanceof FieldError fieldError) {
                                return String.format("Error on field '%s'. Rejected value [%s]; %s.",
                                        fieldError.getField(),
                                        fieldError.getRejectedValue(),
                                        fieldError.getDefaultMessage());
                            }
                            return error.toString();
                        }
                ).toList();
    }
}