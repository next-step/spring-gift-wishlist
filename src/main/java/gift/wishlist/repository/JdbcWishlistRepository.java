package gift.wishlist.repository;

import gift.wishlist.Wishlist;
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
            .usingGeneratedKeyColumns("id")
            .usingColumns("member_id", "item_id");
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", wishlist.getMemberId());
        params.put("item_id", wishlist.getItemId());

        Long generatedValue = simpleInsert.executeAndReturnKey(params).longValue();
        wishlist.setId(generatedValue);

        String sql = "SELECT created_at FROM wishlist WHERE id = ?";
        LocalDateTime createdAt = jdbcTemplate.queryForObject(sql, LocalDateTime.class,
            generatedValue);

        wishlist.setCreatedAt(createdAt);

        return wishlist;
    }

    @Override
    public List<Wishlist> findByMemberIdOrderByCreatedAtDesc(Long memberId) {
        String sql = "SELECT id, member_id, item_id, created_at FROM wishlist WHERE member_id = ? ORDER BY created_at DESC";

        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Wishlist(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("item_id"),
                rs.getTimestamp("created_at").toLocalDateTime()
            ), memberId);
    }

    @Override
    public Optional<Wishlist> findByIdAndMemberId(Long id, Long memberId) {
        String sql = "SELECT id, member_id, item_id, created_at FROM wishlist WHERE id = ? AND member_id = ?";

        try {
            Wishlist wishlist = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Wishlist(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("item_id"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ), id, memberId
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
