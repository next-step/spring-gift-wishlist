package gift.wishlist;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class WishlistDao {
    private final JdbcClient jdbcClient;

    public WishlistDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Wishlist> getWishlistByUserId(UUID userId) {
        return jdbcClient.sql("SELECT * FROM WISHLISTS WHERE user_id = :userId")
                .param("userId", userId)
                .query(Wishlist.class)
                .list();
    }
}
