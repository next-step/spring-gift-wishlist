package gift.wishlist.repository;

import gift.wishlist.Wishlist;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcWishlistRepository implements WishlistRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleInsert;

    public JdbcWishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("wishlist")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", wishlist.getMemberId());
        params.put("item_id", wishlist.getItemId());
        params.put("created_at", Timestamp.valueOf(wishlist.getCreatedAt()));

        Long generatedValue = simpleInsert.executeAndReturnKey(params).longValue();
        wishlist.setId(generatedValue);

        String sql = "SELECT created_at FROM WISHLIST WHERE id = ?";
        LocalDateTime createdAt = jdbcTemplate.queryForObject(sql, LocalDateTime.class,
            generatedValue);

        return new Wishlist(
            generatedValue,
            wishlist.getMemberId(),
            wishlist.getItemId(),
            createdAt
        );
    }

    @Override
    public List<Wishlist> findAllByOrderByCreatedAtDesc() {
        String sql = "SELECT id, member_id, item_id, created_at FROM wishlist ORDER BY created_at DESC";

        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Wishlist(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("item_id"),
                rs.getTimestamp("created_at").toLocalDateTime()
            )
        );
    }

    @Override
    public Optional<Wishlist> findById(Long id) {
        String sql = "SELECT id, member_id, item_id, created_at FROM wishlist WHERE id = ?";

        try {
            Wishlist wishlist = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Wishlist(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("item_id"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ), id
            );
            return Optional.ofNullable(wishlist);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM wishlist WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
