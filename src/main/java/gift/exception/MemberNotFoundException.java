package gift.exception;

public class MemberNotFoundException extends RuntimeException {

    private final Long memberId;

    public MemberNotFoundException(Long memberId) {
        super("사용자 ID가 " + memberId + "인 사용자를 찾을 수 없습니다.");
        this.memberId = memberId;
    }
}