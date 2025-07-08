package gift.entity;

public class Member {
    Long id;
    String email;
    String password;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
