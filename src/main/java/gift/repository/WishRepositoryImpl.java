package gift.repository;

import gift.entity.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Map;

@Repository
public class WishRepositoryImpl implements WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Wish saveWish(Wish wish) {
        String sql = "INSERT INTO wishes (member_id, product_id, created_at) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, wish.getMemberId());
            ps.setLong(2, wish.getProductId());
            ps.setTimestamp(3, Timestamp.valueOf(wish.getCreatedAt()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.get("id") != null) {
            Long id = ((Number) keys.get("id")).longValue();
            return wish.withId(id);
        }
        return wish;
    }

    @Override
    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishes WHERE member_id = ? AND product_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }
}