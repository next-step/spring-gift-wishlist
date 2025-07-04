package gift.entity;

public enum UserRole {
    ROLE_USER("ROLE_USER"), ROLE_MD("ROLE_MD"), ROLE_ADMIN("ROLE_ADMIN");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }
    @Override
    public String toString() {
        return roleName;
    }
}
