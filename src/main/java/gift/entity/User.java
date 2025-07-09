package gift.entity;

public class User {

    private Long id;
    private String email;
    private String password;

    // 정적 팩토리 메서드
    public static User createUser(String email, String password) {
        return new User(null, email, password);
    }

    public static User withId(Long id, String email, String password) {
        return new User(id, email, password);
    }

    private User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
