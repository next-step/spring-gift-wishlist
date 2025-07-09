package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;

public interface MemberServiceInterface {

    boolean isEmailExists(String email);

    MemberResponseDto register(MemberRequestDto requestDto);

    MemberResponseDto login(MemberRequestDto requestDto);
}
