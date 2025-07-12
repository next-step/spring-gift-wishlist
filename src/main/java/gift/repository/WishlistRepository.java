package gift.repository;

import gift.entity.Wishlist;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Wishlist> rowMapper = (rs, rowNum) -> new Wishlist(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id")
    );

    public WishlistRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("wishlist")
                .usingGeneratedKeyColumns("id");
    }

    public Wishlist save(Wishlist wishlist) {
        var params = new MapSqlParameterSource()
                .addValue("member_id", wishlist.getMemberId())
                .addValue("product_id", wishlist.getProductId());
        var key = jdbcInsert.executeAndReturnKey(params);
        wishlist.setId(key.longValue());
        return wishlist;
    }

    public List<Wishlist> findAllByMemberId(Long memberId) {
        var sql = "SELECT * FROM wishlist WHERE member_id = :memberId";
        var params = new MapSqlParameterSource("memberId", memberId);
        return template.query(sql, params, rowMapper);
    }

    public Optional<Wishlist> findById(Long id) {
        var sql = "SELECT * FROM wishlist WHERE id = :id";
        var params = new MapSqlParameterSource("id", id);
        return template.query(sql, params, rowMapper).stream().findFirst();
    }

    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        var sql = "SELECT COUNT(*) FROM wishlist WHERE member_id = :memberId AND product_id = :productId";
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("productId", productId);
        return template.queryForObject(sql, params, Integer.class) > 0;
    }

    public void deleteById(Long id) {
        var sql = "DELETE FROM wishlist WHERE id = :id";
        var params = new MapSqlParameterSource("id", id);
        template.update(sql, params);
    }
}