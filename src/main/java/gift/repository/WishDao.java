package gift.repository;

import gift.model.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public void deleteWish(Long userId, Long productId) {
        jdbcClient.sql("DELETE FROM wish WHERE userid = :userid AND productid = :productid")
                .params(Map.of(
                        "userid", userId,
                        "productid", productId
                ))
                .update();
    }

    public List<Wish> getAllWish(Long userId) {
        return jdbcClient.sql("SELECT * FROM wish WHERE userid = :userid")
                .params(Map.of(
                        "userid", userId
                ))
                .query(Wish.class)
                .list();
    }
}