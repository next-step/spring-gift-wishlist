package gift.repository;

import gift.model.Product;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishlistRepository {
    JdbcClient jdbc;
    public WishlistRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Product> findByUserEmail(String userEmail) {
        return jdbc.sql("SELECT * FROM wishlist WHERE user_email = :email")
                .param("email", userEmail)
                .query(Product.class)
                .list();
    }

    public boolean existsByUserEmailAndProductId(String userEmail, Long productId) {
        return jdbc.sql("SELECT COUNT(*) FROM wishlist WHERE user_email = :email AND product_id = :productId")
                .param("email", userEmail)
                .param("productId", productId)
                .query(Long.class)
                .single() > 0;
    }
    public void save(String email, Product product) {
        jdbc.sql("INSERT INTO wishlist (user_email, product_id) VALUES (:email, :productId)")
                .param("email", email)
                .param("productId", product.getId())
                .update();
    }

    public void deleteByUserEmailAndProductId(String userEmail, Long productId) {
        jdbc.sql("DELETE FROM wishlist WHERE user_email=:email AND product_id=:productId")
                .param("email", userEmail)
                .param("productId", productId)
                .update();
    }

}
