package gift.repository;

import gift.domain.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

public class WishRepositoryImpl implements WishRepository  {

    private final JdbcClient jdbcClient;

    public WishRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Wish wish) {

    }

    @Override
    public void updateQuantity(Long memberId, Long productId, int quantity) {

    }

    @Override
    public void delete(Long memberId, Long productId) {

    }

    @Override
    public List<Wish> findWishByMemberId(Long memberId) {
        return List.of();
    }

    @Override
    public boolean exists(Long memberId, Long productId) {
        return false;
    }
}
