package gift.entity;

import java.util.Objects;

public class Member {

    private Long id;
    private String email;
    private String password;
    private String role;

    public Member(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public Boolean isAdmin(){
        return this.role.equals("admin");
    }
}
