package gift.wishlist.repository;

import gift.wishlist.domain.Wishlist;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistRepository {

    private final JdbcClient jdbcClient;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Wishlist save(Wishlist wishlist) {
        String sql = """
            INSERT INTO wishlist (product_id, member_id, quantity)
            VALUES (?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
            .param(wishlist.getProductId())
            .param(wishlist.getMemberId())
            .param(wishlist.getQuantity())
            .update(keyHolder);

        Long id = keyHolder.getKey().longValue();

        return wishlist.setId(id);
    }

    public Optional<Wishlist> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = """
            SELECT *
            FROM wishlist
            WHERE member_id = ? AND product_id = ?
            """;

        return jdbcClient.sql(sql)
            .param(memberId)
            .param(productId)
            .query(wishlistRowMapper())
            .stream()
            .findFirst();
    }

    public List<Wishlist> findByMemberId(Long memberId) {
        String sql = """
            SELECT *
            FROM wishlist
            WHERE member_id = ?
            """;

        return jdbcClient.sql(sql)
            .param(memberId)
            .query(wishlistRowMapper())
            .list();
    }

    private RowMapper<Wishlist> wishlistRowMapper() {
        return ((rs, rowNum) ->
            new Wishlist(
                rs.getLong("id"),
                rs.getLong("product_id"),
                rs.getLong("member_id"),
                rs.getInt("quantity")
            )
        );
    }
}
