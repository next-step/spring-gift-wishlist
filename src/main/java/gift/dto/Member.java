package gift.dto;

public class Member {
    private Long memberId;
    private String email;
    private String password; // 암호화된 형태로 저장하기

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
