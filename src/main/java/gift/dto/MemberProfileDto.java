package gift.dto;

public class MemberProfileDto {
    private final Long id;
    private final String email;

    public MemberProfileDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }
    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
}
