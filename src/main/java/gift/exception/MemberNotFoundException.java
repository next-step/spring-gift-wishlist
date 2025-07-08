package gift.exception;

public class MemberNotFoundException extends RuntimeException {

    private final Long id;

    public MemberNotFoundException(Long id) {
        super("Member not found with id: " + id);
        this.id = id;
    }
}
