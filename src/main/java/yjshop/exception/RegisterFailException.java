package yjshop.exception;

import ch.qos.logback.core.model.Model;
import org.springframework.validation.BindingResult;
import yjshop.dto.MemberRequestDto;

public class RegisterFailException extends RuntimeException {

    private BindingResult bindingResult;
    private String message;

    public RegisterFailException(BindingResult bindingResult, String message){
        this.bindingResult = bindingResult;
        this.message = message;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
