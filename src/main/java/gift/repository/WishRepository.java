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

    public Wish save(Wish wish) { // 위시리스트 추가
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

    public Optional<Wish> findById(Long id) { // 위시리스트의 특정 상품 조회
        String sql = "SELECT id, member_id, product_id, quantity FROM wish WHERE id = ?";
        List<Wish> results = jdbcTemplate.query(sql, wishRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Wish> findByMemberId(Long memberId) { // 특정 사용자의 위시리스트 조회
        String sql = "SELECT id, member_id, product_id, quantity FROM wish WHERE member_id = ? ORDER BY id DESC"; //최신순으로 정렬
        return jdbcTemplate.query(sql, wishRowMapper, memberId);
    }

    public Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId) { // 특정 사용자의 특정 상품 조회 
        String sql = "SELECT id, member_id, product_id, quantity FROM wish WHERE member_id = ? AND product_id = ?";
        List<Wish> results = jdbcTemplate.query(sql, wishRowMapper, memberId, productId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) { // 특정 사용자의 특정 상품 조회 -> 중복 확인용
        String sql = "SELECT COUNT(*) FROM wish WHERE member_id = ? AND product_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }

    public void updateQuantity(Long id, Integer quantity) { // 위시리스트에 있는 해당 id의 수량 변경
        String sql = "UPDATE wish SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, id);
    }

    public void deleteById(Long id) { // 위시리스트에 있는 해당 id의 상품 삭제
        String sql = "DELETE FROM wish WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
} 