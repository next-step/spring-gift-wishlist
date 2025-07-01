package gift.repository;

import gift.entity.ApprovedProduct;
import org.springframework.jdbc.core.JdbcTemplate;

public class ApprovedProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ApprovedProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 승인 여부 확인
    public boolean isApproved(String name) {
        String sql = "SELECT COUNT(*) FROM approved_products WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    // 승인 상품 등록
    public void save(ApprovedProduct product) {
        String sql = "INSERT INTO approved_products (name) VALUES (?)";
        jdbcTemplate.update(sql, product.getName());
    }

}
