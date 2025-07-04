package gift.domain;

public class User {

    private final Long id;
    private final String email;
    private final String password;

    private User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static User of(Long id, String email, String password) {
        return new User(id, email, password);
    }

    public static User of(String email, String password) {
        return new User(null, email, password);
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
}
