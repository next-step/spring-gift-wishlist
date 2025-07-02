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
    public void saveApprovedProductName(String name) {
        String insertSql = "INSERT INTO approved_products(name) VALUES(?)";
        jdbcTemplate.update(insertSql, name);
    }

    @Override
    public boolean existApprovedProductName(String name) {
        String sql = "SELECT COUNT(*) FROM approved_products WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);

        return (count != null && count > 0);

    }
}
