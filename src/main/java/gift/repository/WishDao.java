package gift.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class WishDao {
    private JdbcClient jdbcClient;

    public WishDao(JdbcClient jdbcClient) {this.jdbcClient = jdbcClient;}

    public void addWish(Long userId, Long productId) {
        jdbcClient.sql("INSERT INTO wish (userid, productid) VALUES (:userid, :productid)")
                .params(Map.of(
                        "userid", userId,
                        "productid", productId
                ))
                .update();
    }
}