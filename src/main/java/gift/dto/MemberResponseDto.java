package gift.dto;

public class MemberResponseDto {

    private String token;


    public MemberResponseDto() {
    }

    public MemberResponseDto(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
