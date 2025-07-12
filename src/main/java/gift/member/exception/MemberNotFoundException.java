package gift.member.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long id) {
        super("해당 회원을 찾을 수 없습니다. id=" + id);
    }

    public MemberNotFoundException(String email) {
        super("해당 이메일을 가진 회원을 찾을 수 없습니다. email=" + email);
    }
}
