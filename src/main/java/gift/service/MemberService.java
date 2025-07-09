package gift.service;

import gift.dto.request.MemberRequest;
import gift.dto.response.MemberResponse;

public interface MemberService{
    MemberResponse register(MemberRequest request);
    MemberResponse login(MemberRequest request);
}
