package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;

public interface MemberService {

    String saveMember(MemberRequestDto memberRequestDto);

    String existMember(MemberRequestDto memberRequestDto);

    Member findByEmail(String email);
}
