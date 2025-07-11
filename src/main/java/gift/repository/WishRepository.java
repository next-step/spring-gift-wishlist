package gift.repository;


import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public class WishRepository {
    private final JdbcClient jdbcClient;

    public WishRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void save(Long memberId, Long productId, int quantity) {
        String sql = """
            INSERT INTO Wish (member_id, product_id, quantity)
            VALUES (:memberId, :productId, :quantity)
        """;

        jdbcClient.sql(sql)
                .param("memberId", memberId)
                .param("productId", productId)
                .param("quantity", quantity)
                .update();
    }

}
