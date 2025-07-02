package gift.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ApprovedProductRepositoryImpl implements ApprovedProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ApprovedProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveApprovedProductName(String productName) {
        String insertSql = "INSERT INTO approved_product_names(name) VALUES(?)";
        jdbcTemplate.update(insertSql, productName);
    }

    @Override
    public boolean existApprovedProductName(String name) {
        String sql = "SELECT COUNT(*) FROM approved_product_names WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);

        return (count != null && count > 0);

    }
}
