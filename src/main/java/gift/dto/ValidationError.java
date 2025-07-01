package gift.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationError(
    String field,
    String message
) {
    public static ValidationError from(FieldError fieldError) {
        return new ValidationError(
            fieldError.getField(),
            fieldError.getDefaultMessage()
        );
    }

    public static ValidationError from(ObjectError objectError) {
        return new ValidationError(
                null,
                objectError.getDefaultMessage()
        );
    }

    public static List<ValidationError> from(ParameterValidationResult validationResult) {
        String parameterName = validationResult.getMethodParameter().getParameterName();
        return validationResult.getResolvableErrors()
            .stream()
            .map(error -> new ValidationError(parameterName, error.getDefaultMessage()))
            .toList();
    }


}
