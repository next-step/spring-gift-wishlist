package gift.dto.user;

public class UserResponseDto {
    private final String token;

    public UserResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
