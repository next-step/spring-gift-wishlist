package gift.wishlist.repository;

import gift.product.entity.Product;
import gift.wishlist.entity.Wishlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishlistRepository {
    private static final Logger log = LoggerFactory.getLogger(WishlistRepository.class);
    private final JdbcClient jdbcClient;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Wishlist saveWish(Long memberId, Long productId, int quantity) {
        String sql = """
            INSERT INTO wishlist (member_id, product_id, quantity)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE quantity = quantity + VALUES (quantity)
        """;

        jdbcClient.sql(sql)
                .param(memberId)
                .param(productId)
                .param(quantity);

        return findWishByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new IllegalStateException("저장 작업 후 데이터를 찾지 못했습니다."));

    }

    public Optional<Wishlist> findWishByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = """
                SELECT wishlist_id, member_id, product_id, quantity
                FROM wishlist
                WHERE member_id = ?
                AND product_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(memberId)
                .param(productId)
                .query(getRowMapper())
                .optional();
    }

    public List<Wishlist> findAllByMemberId(Long memberId) {
        String sql = """
                SELECT wishlist_id, member_id, product_id, quantity
                FROM wishlist
                WHERE member_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(memberId)
                .query(getRowMapper())
                .list();
    }

    private static RowMapper<Wishlist> getRowMapper(){
        return (rs, rowNum) -> new Wishlist(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity")
        );
    }
}
