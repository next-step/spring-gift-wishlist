package gift.repository;

import gift.domain.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Wish> wishRowMapper = (rs, rowNum) ->
            new Wish(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id")
            );

    public WishRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("wish")
                .usingGeneratedKeyColumns("id");
    }

    public Wish save(Long memberId, Long productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", memberId);
        params.put("product_id", productId);

        Number key = jdbcInsert.executeAndReturnKey(params);
        return new Wish(key.longValue(), memberId, productId);
    }

    public void delete(Long memberId, Long productId) {
        jdbcTemplate.update(
                "DELETE FROM wish WHERE member_id = ? AND product_id = ?",
                memberId, productId
        );
    }

    public List<Wish> findByMemberId(Long memberId) {
        return jdbcTemplate.query(
                "SELECT * FROM wish WHERE member_id = ?",
                wishRowMapper,
                memberId
        );
    }
}
