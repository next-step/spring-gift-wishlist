package gift.repository;

import gift.entity.Wish;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public WishRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("wishes")
            .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Wish> wishRowMapper = (rs, rowNum) -> new Wish(
        rs.getLong("id"),
        rs.getLong("member_id"),
        rs.getLong("product_id"),
        rs.getInt("quantity")
    );

    public Optional<Wish> findById(Long id) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wishes WHERE id = :id";
        return jdbcClient.sql(sql)
            .param("id", id)
            .query(wishRowMapper)
            .optional();
    }

    public List<Wish> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wishes WHERE member_id = :memberId";
        return jdbcClient.sql(sql)
            .param("memberId", memberId)
            .query(wishRowMapper)
            .list();
    }

    public Wish save(Wish wish) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", wish.getMemberId());
        params.put("product_id", wish.getProductId());
        params.put("quantity", wish.getQuantity());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Wish(id, wish.getMemberId(), wish.getProductId(), wish.getQuantity());
    }

    public void update(Wish wish) {
        String sql = "UPDATE wishes SET quantity = :quantity WHERE id = :id";
        jdbcClient.sql(sql)
            .param("quantity", wish.getQuantity())
            .param("id", wish.getId())
            .update();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM wishes WHERE id = :id";
        jdbcClient.sql(sql)
            .param("id", id)
            .update();
    }
}