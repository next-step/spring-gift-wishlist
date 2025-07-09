package gift.repository;

import gift.domain.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepositoryImpl implements WishRepository  {

    private final JdbcClient jdbcClient;

    public WishRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Long memberId, Long productId, int quantity) {
        jdbcClient.sql("""
            INSERT INTO wishes (member_id, product_id, quantity)
            VALUES (:memberId, :productId, :quantity)
            """)
                .param("memberId", memberId)
                .param("productId", productId)
                .param("quantity", quantity)
                .update();
    }

    @Override
    public void updateQuantity(Long memberId, Long productId, int quantity) {
        jdbcClient.sql("""
            UPDATE wishes
            SET quantity = :quantity
            WHERE member_id = :memberId AND product_id = :productId
            """)
                .param("quantity", quantity)
                .param("memberId", memberId)
                .param("productId", productId)
                .update();

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
        Integer count = jdbcClient.sql("""
            SELECT COUNT(*) FROM wishes
            WHERE member_id = :memberId AND product_id = :productId
            """)
                .param("memberId", memberId)
                .param("productId", productId)
                .query(Integer.class)
                .single();

        return count != null && count > 0;
    }
}
