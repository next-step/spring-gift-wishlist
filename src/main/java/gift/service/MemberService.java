package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;

import java.lang.reflect.Member;

public interface MemberService {

    void saveMember(MemberRequestDto memberRequestDto);

    void login(MemberRequestDto memberRequestDto);
}
