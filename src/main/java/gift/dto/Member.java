package gift.dto;

public class Member {
    private Long memberId;
    private String email;
    private String password; // 암호화된 형태로 저장하기

    public Member(Long memberId, String email, String password){
        this.memberId = memberId;
        this.email = email;
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
