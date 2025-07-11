package gift.entity;

public class Member {

    private Long id;
    private String email;
    private String password;

    // BeanPropertyRowMapper가 객체를 생성하기 위해선 삭제하면 안됨
    public Member() {
    }

    private Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static Member from(String email, String password) {
        return new Member(null, email, password);
    }

    public Member assignId(Long id) {
        return new Member(id, this.email, this.password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}