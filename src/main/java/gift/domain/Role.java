package gift.domain;

public enum Role {
  USER("사용자"),
  ADMIN("관리자");

  private final String roleName;

  Role(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleName() {
    return roleName;
  }

  public static Role from(String roleName) {
    for (Role role : Role.values()) {
      if (role.roleName.equals(roleName)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Unknown role name: " + roleName);
  }
}
