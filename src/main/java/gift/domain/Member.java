package gift.domain;

public class Member {
    
    private Long id;
    private String email;
    private String password;

    public Member(Long id, String email, String password) {
        validateId(id);
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static Member of(String email, String password) {
        return new Member(null, email, password);
    }

    public static Member withId(Long id, String email, String password) {
        return new Member(id, email, password);
    }

    public Long id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    private static void validateId(Long id) {
        if (id != null && id < 0) {
            throw new IllegalArgumentException("ID는 음수일 수 없습니다.");
        }
    }
}
