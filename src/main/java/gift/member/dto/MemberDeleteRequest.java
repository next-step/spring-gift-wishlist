package gift.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberDeleteRequest {

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;


    public MemberDeleteRequest(String password) {
        this.password = password;
    }

    protected MemberDeleteRequest(){}

    public String getPassword() {
        return password;
    }

}
