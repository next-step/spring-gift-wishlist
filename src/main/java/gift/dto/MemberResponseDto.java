package gift.dto;

public class MemberResponseDto {
    String token;

    public MemberResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
