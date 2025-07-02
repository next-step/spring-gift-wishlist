package gift.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

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
    public void existApprovedProductName(String name) {
        String sql = "SELECT COUNT(*) FROM approved_product_names WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);

        if (count != null && count > 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "해당 상품명은 이미 등록되어 있습니다."
            );
        }
    }
}
