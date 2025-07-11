package gift.entity;

import gift.dto.Role;

public class Member {
    private Long memberId;
    private String email;
    private String password; // 암호화된 형태로 저장하기
    private Role role = Role.USER; //회원 가입 -> 일반 회원

    public Member(Long memberId, String email, String password, Role role){
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Long getMemberId(){
        return memberId;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public Role getRole() {
        return role;
    }
}