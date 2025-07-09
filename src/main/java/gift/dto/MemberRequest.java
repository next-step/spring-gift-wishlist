package gift.dto;

public class MemberRequest {
    private final String id;
    private final String password;

    public MemberRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() { return id; }
    public String getPassword() { return password; }
}

