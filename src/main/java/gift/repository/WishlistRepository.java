package gift.repository;

import java.util.List;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import gift.domain.Product;

@Repository
public class WishlistRepository {

    private final JdbcClient jdbcClient;
    private final RowMapper<Product> rowMapper;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.rowMapper = new DataClassRowMapper<>(Product.class);
    }

    public boolean existsByMemberId(Long memberId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE memberId = :memberId";
        return jdbcClient.sql(sql)
            .param("memberId", memberId)
            .query(Long.class)
            .single() > 0;
    }

    public List<Product> findAllProductByMemberId(Long memberId) {
        String sql = """
        SELECT p.id, p.name, p.price, p.imageUrl
        FROM wishlist AS w
        JOIN product AS p ON w.productId = p.id
        WHERE memberId = :memberId
        """;
        return jdbcClient.sql(sql)
            .param("memberId", memberId)
            .query(rowMapper)
            .list();
    }

    public int addProductToWishlist(Long memberId, Long productId) {
        String sql = "INSERT INTO wishlist (memberId, productId) VALUES (:memberId, :productId)";

        return jdbcClient.sql(sql)
            .param("memberId", memberId)
            .param("productId", productId)
            .update();
    }
}
