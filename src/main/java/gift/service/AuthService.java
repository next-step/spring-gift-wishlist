package gift.service;

import gift.dto.AuthRequest;
import gift.dto.AuthResponse;

public interface AuthService {

    AuthResponse register(AuthRequest request);

    AuthResponse login(AuthRequest request);
}
