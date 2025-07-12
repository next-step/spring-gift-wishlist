package gift.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class WishJdbcRepository implements WishRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final UserJdbcRepository userJdbcRepository;
    private final ProductJdbcRepository productJdbcRepository;

    public WishJdbcRepository(JdbcTemplate jdbcTemplate, UserJdbcRepository userJdbcRepository,
        ProductJdbcRepository productJdbcRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
            .withTableName("users")
            .usingColumns("user_role", "email", "password");
        this.userJdbcRepository = userJdbcRepository;
        this.productJdbcRepository = productJdbcRepository;
    }

}
