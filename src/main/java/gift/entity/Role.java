package gift.entity;

public enum Role {
    ADMIN,
    USER;

    public static boolean isValid(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
