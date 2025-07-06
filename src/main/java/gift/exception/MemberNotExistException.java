package gift.exception;

public class MemberNotExistException extends RuntimeException {
    public MemberNotExistException(Long memberId) {

        super("회원 ID " + memberId + "에 해당하는 회원이 존재하지 않습니다.");
    }
}
