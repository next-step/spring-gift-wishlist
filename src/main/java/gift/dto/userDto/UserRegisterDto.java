package gift.dto.userDto;

import gift.validation.userPolicy.UserFieldValid;

@UserFieldValid
public record UserRegisterDto( String email, String password) {
}
