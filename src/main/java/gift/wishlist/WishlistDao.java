package gift.wishlist;

import gift.user.domain.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WishlistDao {
    private final JdbcClient jdbcClient;

    public WishlistDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Wishlist> getWishlistByUserId(UUID userId) {
        return jdbcClient.sql("SELECT * FROM WISHLISTS WHERE userId = :userId")
                .param("userId", userId)
                .query(Wishlist.class)
                .list();
    }

    public Wishlist save(Wishlist wishlist) {
        jdbcClient.sql("INSERT INTO WISHLISTS (userId, productId) VALUES (:userId, :productId)")
                .param("userId", wishlist.getUserId())
                .param("productId", wishlist.getProductId())
                .update();

        return wishlist;
    }

    public Optional<Wishlist> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM WISHLISTS WHERE id = :id")
                .param("id", id)
                .query(Wishlist.class)
                .optional();
    }

    public void delete(Long id) {
        jdbcClient.sql("DELETE FROM WISHLISTS WHERE id = :id")
                .param("id", id)
                .update();
    }
}
