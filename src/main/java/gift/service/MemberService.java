package gift.service;

import gift.dto.AuthToken;
import gift.dto.AuthRequest;

public interface MemberService {
    AuthToken register(AuthRequest request);
}