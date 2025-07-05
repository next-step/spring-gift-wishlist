package gift.exception;

import org.springframework.validation.BindingResult;

public class RequestNotValidException extends RuntimeException {
    private final BindingResult bindingResult;

    public RequestNotValidException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    @Override
    public String getMessage() {
        StringBuilder errorMessageBuilder = new StringBuilder();
        bindingResult.getFieldErrors().forEach( fieldError -> {
            errorMessageBuilder.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append(", ");
        });
        return errorMessageBuilder.toString();
    }
}
