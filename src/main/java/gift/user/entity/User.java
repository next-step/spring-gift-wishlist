package gift.user.entity;

public class User {
  private Long id;
  private String email;
  private String password;
  private Role role;

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }

  public User(Long id, String email, String password, Role role) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public boolean isEqualPassword(String password) {
    return this.password.equals(password);
  }
}
