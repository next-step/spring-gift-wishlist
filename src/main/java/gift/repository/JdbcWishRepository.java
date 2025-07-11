package gift.repository;

import gift.entity.Wish;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcWishRepository implements WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcWishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Wish save(Wish wish) {
        String sql = "INSERT INTO wishes (member_id, product_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, wish.getMemberId());
            ps.setLong(2, wish.getProductId());
            return ps;
        }, keyHolder);

        return wish;
    }
}
