package gift.repository;


import gift.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findProductsByMemberId(Long memberId) {
        String sql = "SELECT p.id, p.name, p.price, p.image_url FROM product p " +
                "JOIN wish w ON p.id = w.product_id WHERE w.member_id = ?";
        return jdbcTemplate.query(sql, productRowMapper, memberId);
    }

    public void save(Long memberId, Long productId) {
        String sql = "INSERT INTO wish (member_id, product_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM wish WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }
}