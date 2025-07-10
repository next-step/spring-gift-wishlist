package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;

import java.lang.reflect.Member;

public interface MemberService {

    String saveMember(MemberRequestDto memberRequestDto);

    String existMember(MemberRequestDto memberRequestDto);
}
