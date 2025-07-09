package gift.domain;

public record Member(Long id, String email, String password) {

    public Member {
        validateId(id);
    }

    public static Member of(String email, String password) {
        return new Member(null, email, password);
    }

    public static Member withId(Long id, String email, String password) {
        validateId(id);
        return new Member(id, email, password);
    }

    private static void validateId(Long id) {
        if (id != null && id < 0) {
            throw new IllegalArgumentException("ID는 음수일 수 없습니다.");
        }
    }

    public boolean isMatchPassword(String password) {
        return this.password.equals(password);
    }
}
