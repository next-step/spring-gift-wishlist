package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;

public interface MemberService {

    public MemberResponseDto create(MemberRequestDto requestDto);

    public MemberResponseDto login(MemberRequestDto requestDto);

    void changePassword(MemberRequestDto requestDto);

    void resetPassword(MemberRequestDto requestDto);
}