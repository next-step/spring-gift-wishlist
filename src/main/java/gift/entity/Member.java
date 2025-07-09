package gift.entity;

public class Member {

    private Long id;
    private String email;
    private String password;

    // 정적 팩토리 메서드
    public static Member createMember(String email, String password) {
        return new Member(null, email, password);
    }

    public static Member withId(Long id, String email, String password) {
        return new Member(id, email, password);
    }

    private Member(Long id, String email, String password) {
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

    public void validatePassword(String password) throws IllegalAccessException {
        if(!this.password.equals(password)) {
            throw new IllegalAccessException("비밀번호가 일치하지 않습니다.");
        }
    }
}
