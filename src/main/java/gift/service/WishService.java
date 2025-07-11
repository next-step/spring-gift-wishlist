package gift.service;

import gift.jwt.JwtTokenProvider;
import gift.model.Wish;
import gift.repository.WishDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    private final WishDao wishDao;
    private final JwtTokenProvider jwtTokenProvider;

    public WishService(WishDao wishDao, JwtTokenProvider jwtTokenProvider) {
        this.wishDao = wishDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public void addWish(Long id, Long productid) {
        wishDao.addWish(id, productid);
    }

    public void deleteWish(Long id, Long productid) {
        wishDao.deleteWish(id, productid);
    }

    public List<Wish> getAllWish(Long id) {
        return wishDao.getAllWish(id);
    }
}