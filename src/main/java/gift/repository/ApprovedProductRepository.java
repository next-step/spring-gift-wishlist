package gift.repository;

import org.springframework.jdbc.core.JdbcTemplate;

public class ApprovedProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ApprovedProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
