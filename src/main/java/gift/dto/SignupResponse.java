package gift.dto;

import gift.domain.User;

public record SignupResponse(Long id, String email) {

    public static SignupResponse from(User user) {
        return new SignupResponse(user.getId(), user.getEmail());
    } // password는 념겨주지 않는게 좋아보인다. 그런데 id를 넘겨줘야하나? PK는 노출되지 않도록 하는 편이 낫지 않을까?
}
