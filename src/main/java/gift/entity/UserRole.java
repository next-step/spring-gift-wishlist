package gift.entity;

public enum UserRole {
    ROLE_USER("ROLE_USER"), ROLE_MD("ROLE_MD"), ROLE_ADMIN("ROLE_ADMIN");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public static UserRole fromString(String roleName) {
        for (UserRole role : UserRole.values()) {
            if (role.roleName.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("해당 상수가 존재하지 않습니다."
                + UserRole.class.getCanonicalName() + "." + roleName);
    }

    @Override
    public String toString() {
        return roleName;
    }
}
