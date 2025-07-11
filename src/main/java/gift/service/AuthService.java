package gift.service;

import gift.dto.AuthRequest;
import gift.dto.AuthResponse;
import gift.entity.User;

public interface AuthService {

    void register(AuthRequest request);

    AuthResponse login(AuthRequest request);

    User findByToken(String token);
}
