package gift.service.auth;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;

public interface AuthService {
    MemberResponseDto login(MemberRequestDto requestDto);
}
