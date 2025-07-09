package gift.entity;

public class Member {

    private Long identifyNumber;
    private String email;
    private String password;
    private Role authority;

    public Member() {}

    public Member(Long identifyNumber, String email, String password, Role authority) {
        this.identifyNumber = identifyNumber;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.authority = Role.ROLE_USER;
    }

    public Member updateIdentifyNumber(Long identifyNumber) {
        return new Member(identifyNumber, email, password, authority);
    }

    public Long getIdentifyNumber() {
        return identifyNumber;
    }

    public Member updateEmail(String email) {
        return new Member(identifyNumber, email, password, authority);
    }

    public String getEmail() {
        return email;
    }

    public Role getAuthority() {
        return authority;
    }

    public Member updatePassword(String password) {
        return new Member(identifyNumber, email, password, authority);
    }

    public String getPassword() {
        return password;
    }
}
