package gift.entity;

import gift.dto.MemberRequestDto;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member(MemberRequestDto memberRequestDto) {
        email = memberRequestDto.email();
        password = memberRequestDto.password();
    }

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
