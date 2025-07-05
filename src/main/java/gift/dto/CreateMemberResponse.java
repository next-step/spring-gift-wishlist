package gift.dto;

import gift.entity.Member;

public record CreateMemberResponse(
        Long identifyNumber,
        String email
) {
    public static CreateMemberResponse from(Member member) {
        return new CreateMemberResponse(member.getIdentifyNumber(), member.getEmail());
    }
}
