package gift.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;

    private Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // 정적 팩토리 메소드 사용을 통해 객체 생성 전에 유효성 검사를 진행
    public static Member of(Long id, String email, String password) {
        validateEmail(email);
        validatePassword(password);

        return new Member(id, email, password);
    }

    // 정적 팩토리 메소드 사용을 통해 객체 생성 전에 유효성 검사를 진행
    // 회원가입용 - ID 없이 생성
    public static Member create(String email, String password) {
        validateEmail(email);
        validatePassword(password);

        return new Member(null, email, password);
    }

    // 도메인 레벨에서 유효성 검사 진행
    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    // 도메인 레벨에서 유효성 검사 진행
    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (password.length() < 4) {
            throw new IllegalArgumentException("비밀번호는 최소 4자 이상이어야 합니다.");
        }
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}