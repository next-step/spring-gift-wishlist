package gift.service.member;

import gift.dto.member.AuthResponse;
import gift.dto.member.RegisterRequest;

public interface MemberService {

    AuthResponse register(RegisterRequest req);


    AuthResponse login(String email, String rawPassword);

}
