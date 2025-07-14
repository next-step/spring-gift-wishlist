package gift.dto;

import gift.Entity.Member;

public class TokenResponse {
    private String token;
    private Member member;

    public TokenResponse(String token, Member member) {
        this.token = token;
        this.member = member;
    }

    public String getToken() {
        return token;
    }
    public Member getMember() {
        return member;
    }
}
