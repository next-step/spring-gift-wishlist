package gift.service.auth;

import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;

public interface AuthService {
    MemberResponseDto login(LoginRequestDto requestDto);
    
    void checkPermissonForAdmin(String authorizationHeader);
    
    Long checkPermissonForUser(String authorizationHeader);
}
