package gift.service;

import gift.dto.MemberPasswordChangeDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;

public interface MemberService {

    public MemberResponseDto create(MemberRequestDto requestDto);

    public MemberResponseDto login(MemberRequestDto requestDto);

    void changePassword(MemberPasswordChangeDto requestDto);

    void resetPassword(MemberRequestDto requestDto);
}