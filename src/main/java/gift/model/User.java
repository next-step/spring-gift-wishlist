package gift.model;

public class User {
    private Long id;
    private String userid;
    private String password;
    private String role;

    public Long getId() { return id; }
    public String getUserid() { return userid; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setId(Long id) { this.id = id; }
    public void setUserid(String userid) { this.userid = userid; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}