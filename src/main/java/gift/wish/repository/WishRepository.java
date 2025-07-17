package gift.wish.repository;

import gift.product.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setImgUrl(rs.getString("img_url"));
        return product;
    };

    public List<Product> findAllProductsByMemberId(Long memberId) {
        String sql = """
                SELECT p.id, p.name, p.price, p.img_url
                FROM products p
                JOIN wish w ON p.id = w.product_id
                WHERE w.member_id = ?
                """;
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
