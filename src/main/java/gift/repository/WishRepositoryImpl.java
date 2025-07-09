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
        return jdbcClient.sql("""
                SELECT id, member_id, product_id, quantity
                FROM wishes
                WHERE member_id = :memberId
                """)
                .param("memberId", memberId)
                .query((rs, rowNum) -> new Wish(
                        rs.getLong("id"),
                        rs.getLong("member_id"),
                        rs.getLong("product_id"),
                        rs.getInt("quantity")
                ))
                .list();
    }

    @Override
    public boolean exists(Long memberId, Long productId) {
        return false;
    }
}
