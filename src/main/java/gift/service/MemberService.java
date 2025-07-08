package gift.service;

public interface MemberService{
    void register(String email, String pwd);
    String login(String email, String pwd);
}
