package gift.dto;

public class MemberLoginResponseDto {
    private String token;


    public MemberLoginResponseDto() {
    }

    public MemberLoginResponseDto(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
