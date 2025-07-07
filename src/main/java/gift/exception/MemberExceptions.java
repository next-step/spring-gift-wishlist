package gift.exception;

public class MemberExceptions {
    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String email) {
            super("이미 가입된 이메일입니다: " + email);
        }
    }

    static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException() {
            super("비밀번호가 일치하지 않습니다.");
        }
    }

    static class MemberNotFoundException extends RuntimeException {
        public MemberNotFoundException(String email) {
            super("해당 이메일의 사용자를 찾을 수 없습니다: " + email);
        }
    }
}
