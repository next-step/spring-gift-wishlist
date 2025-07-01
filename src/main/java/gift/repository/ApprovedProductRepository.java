package gift.repository;

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

}
