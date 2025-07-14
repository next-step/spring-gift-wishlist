package gift.repository;

import java.util.List;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import gift.domain.WishItem;

@Repository
public class WishlistRepository {

    private final JdbcClient jdbcClient;
    private final RowMapper<WishItem> rowMapper;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.rowMapper = new DataClassRowMapper<>(WishItem.class);
    }

    public List<WishItem> findAllProductByWishlistId(Long wishlistId) {
        String sql = """
        SELECT p.id AS productId, p.name, p.price, p.imageUrl, w.quantity
        FROM wishlist_item AS w
        JOIN product AS p ON w.productId = p.id
        WHERE w.wishlistId = :wishlistId
        """;

        return jdbcClient.sql(sql)
            .param("wishlistId", wishlistId)
            .query(rowMapper)
            .list();
    }

    public int addProductToWishlist(Long wishlistId, Long productId) {
        String sql = "INSERT INTO wishlist_item (wishlistId, productId, quantity) VALUES (:wishlistId, :productId, 1)";

        return jdbcClient.sql(sql)
            .param("wishlistId", wishlistId)
            .param("productId", productId)
            .update();
    }

    public int deleteProductFromWishlist(Long wishlistId, Long productId) {
        String sql = "DELETE FROM wishlist_item WHERE wishlistId = :wishlistId AND productId = :productId";

        return jdbcClient.sql(sql)
            .param("wishlistId", wishlistId)
            .param("productId", productId)
            .update();
    }
}
