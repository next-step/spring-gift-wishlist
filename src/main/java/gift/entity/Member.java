package gift.entity;

public class Member {

    private Long identifyNumber;
    private String email;
    private String password;

    public Member() {}

    public Member(Long identifyNumber, String email, String password) {
        this.identifyNumber = identifyNumber;
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member updateIdentifyNumber(Long identifyNumber) {
        return new Member(identifyNumber, email, password);
    }

    public Long getIdentifyNumber() {
        return identifyNumber;
    }

    public Member updateEmail(String email) {
        return new Member(identifyNumber, email, password);
    }

    public String getEmail() {
        return email;
    }

    public Member updatePassword(String password) {
        return new Member(identifyNumber, email, password);
    }

    public String getPassword() {
        return password;
    }

}
