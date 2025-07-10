package gift.dto;

import gift.entity.Member;

public class MemberDto {

    private Long id;

    private String email;

    private String password;

    public MemberDto() {}

    public MemberDto(Member member) {
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
