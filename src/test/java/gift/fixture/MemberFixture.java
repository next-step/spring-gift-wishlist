package gift.fixture;

import gift.dto.request.MemberRequsetDto;

public class MemberFixture {
    public static MemberRequsetDto createMember(){
        return new MemberRequsetDto("song@naver.com","1234");

    }
}
