package gift.repository;

import gift.entity.Wish;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Long memberId, Long productId) {
        String sql = "INSERT INTO wish (member_id, product_id, quantity) VALUES (?, ?, 1)";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM wish WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public boolean exists(Long memberId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wish WHERE member_id = ? AND product_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }

    public List<Wish> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM wish WHERE member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Wish(
                rs.getLong("id"),
                rs.getLong("product_id"),
                rs.getLong("member_id")
        ), memberId);
    }
}
