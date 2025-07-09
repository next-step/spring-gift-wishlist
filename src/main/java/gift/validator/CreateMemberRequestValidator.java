package gift.validator;

import gift.dto.CreateMemberRequest;
import gift.repository.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CreateMemberRequestValidator implements ConstraintValidator<ValidCreateMemberRequest, CreateMemberRequest> {

    private final MemberRepository memberRepository;

    public CreateMemberRequestValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean isValid(CreateMemberRequest createMemberRequest, ConstraintValidatorContext context) {
        if (createMemberRequest == null) {
            return true;
        }

        String email = createMemberRequest.email();
        if (email != null && memberRepository.checkEmailExists(email)) {
            addConstraintViolation(context, "email", "이미 존재하는 이메일입니다.");
            return false;
        }

        String password = createMemberRequest.password();
        if (password != null && (password.length() < 15 || password.length() > 64)) {
            addConstraintViolation(context, "password", "비밀번호는 15자 이상 64자 이내여야 합니다.");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String fieldName, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(fieldName)
                .addConstraintViolation();
    }
}
