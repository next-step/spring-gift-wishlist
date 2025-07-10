package gift.repository;

import gift.dto.WishRequestDto;
import gift.entity.Wish;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepository {
    private final JdbcClient jdbcClient;

    public WishRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Wish> findByMemberId(Long memberId) {
        String sql = """
            SELECT id, member_id, product_id FROM WISHLIST WHERE member_id = :id
            """;
        return jdbcClient.sql(sql)
                .param("id", memberId)
                .query(wishRowMapper())
                .list();
    }

    public void addWish(Long memberId, Long productId) {
        String sql = """
                INSERT INTO WISHLIST (member_id, product_id) VALUES (:member_id, :product_id)
                """;

        jdbcClient.sql(sql)
                .param("member_id", memberId)
                .param("product_id", productId)
                .update();
    }

    public void deleteWish(Long memberId, Long productId) {
        String sql = """
                DELETE FROM WISHLIST WHERE member_id = :member_id AND product_id = :product_id
                """;

        jdbcClient.sql(sql)
                .param("member_id", memberId)
                .param("product_id", productId)
                .update();
    }

    public boolean existWish(Long memberId, Long productId) {
        String sql = """
                SELECT COUNT(*) FROM WISHLIST WHERE member_id = :member_id AND  product_id = :product_id
                """;

        Integer count = jdbcClient.sql(sql)
                .param("member_id", memberId)
                .param("product_id", productId)
                .query(Integer.class)
                .single();

        return count > 0;
    }

    private RowMapper<Wish> wishRowMapper() {
        return (rs, rowNum) -> new Wish(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id")
                );
    }
}
