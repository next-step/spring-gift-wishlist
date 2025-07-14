package gift.repository;

import gift.entity.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.List;
import java.util.Optional;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Wish> wishRowMapper = (rs, rowNum) -> {
        Wish wish = new Wish();
        wish.setId(rs.getLong("id"));
        wish.setMemberId(rs.getLong("member_id"));
        wish.setProductId(rs.getLong("product_id"));
        wish.setQuantity(rs.getInt("quantity"));
        return wish;
    };

    public Wish save(Wish wish) {
        String sql = "INSERT INTO wish (member_id, product_id, quantity) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, wish.getMemberId());
            ps.setLong(2, wish.getProductId());
            ps.setInt(3, wish.getQuantity());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        wish.setId(key != null ? key.longValue() : null);
        return wish;
    }

    public Optional<Wish> findById(Long id) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wish WHERE id = ?";
        List<Wish> results = jdbcTemplate.query(sql, wishRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Wish> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wish WHERE member_id = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, wishRowMapper, memberId);
    }

    public Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wish WHERE member_id = ? AND product_id = ?";
        List<Wish> results = jdbcTemplate.query(sql, wishRowMapper, memberId, productId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wish WHERE member_id = ? AND product_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }

    public void updateQuantity(Long id, Integer quantity) {
        String sql = "UPDATE wish SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM wish WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "DELETE FROM wish WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }
} 