package gift.service;

import gift.jwt.JwtTokenProvider;
import gift.repository.WishDao;
import org.springframework.stereotype.Service;

@Service
public class WishService {
    private final WishDao wishDao;
    private final JwtTokenProvider jwtTokenProvider;

    public WishService(WishDao wishDao, JwtTokenProvider jwtTokenProvider) {
        this.wishDao = wishDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }


}