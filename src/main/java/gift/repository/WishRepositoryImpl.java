package gift.repository;

import gift.entity.Wish;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepositoryImpl implements WishRepository {
    private final JdbcClient jdbcClient;

    private static RowMapper<Wish> wishRowMapper() {
        return (rs, rowNum) -> new Wish(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id")
        );
    }

    public WishRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Wish createWish(Wish wish) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("INSERT INTO wish (member_id, product_id) VALUES (:memberId, :productId)")
                .param("memberId", wish.getMemberId())
                .param("productId", wish.getProductId())
                .update(keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Wish(id, wish.getMemberId(), wish.getProductId());
    }

    @Override
    public List<Wish> findAllWishByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id FROM wish WHERE member_id = :memberId";
        return jdbcClient.sql(sql)
                .param("memberId", memberId)
                .query(wishRowMapper())
                .list();
    }

    @Override
    public boolean existsWishById(Long id) {
        String sql = "SELECT COUNT(*) FROM wish WHERE id = :id";

        Integer count = jdbcClient.sql(sql)
                .param("id", id)
                .query(Integer.class)
                .single();

        return count != null && count > 0;
    }

    @Override
    public boolean existsWishByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wish WHERE member_id = :memberId AND product_id = :productId";

        Integer count = jdbcClient.sql(sql)
                .param("memberId", memberId)
                .param("productId", productId)
                .query(Integer.class)
                .single();

        return count != null && count > 0;
    }

    @Override
    public void deleteWishById(Long id) {
        var sql = "DELETE FROM wish WHERE id = :id";
        jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }
}
