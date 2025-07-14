package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;

public interface MemberService {

    void saveMember(MemberRequestDto memberRequestDto);

    boolean existMember(MemberRequestDto memberRequestDto);

    Member findByEmail(String email);
}
