package gift.dto;

import gift.Entity.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishDto {
    private final JdbcClient client;

    public WishDto(JdbcClient client) {
        this.client = client;
    }

    public void insertWish(String memberId, Long productId) {
        var sql = "INSERT INTO wish (member_id, product_id) VALUES (:memberId, :productId)";
        client.sql(sql)
                .param("memberId", memberId)
                .param("productId", productId)
                .update();
    }

    public void deleteWish(String memberId, Long productId) {
        var sql = "DELETE FROM wish WHERE member_id = :memberId AND product_id = :productId";
        client.sql(sql)
                .param("memberId", memberId)
                .param("productId", productId)
                .update();
    }

    public List<Product> findWishesByMember(String memberId) {
        var sql = """
            SELECT p.id, p.name, p.price, p.imageUrl
            FROM products p
            JOIN wish w ON p.id = w.product_id
            WHERE w.member_id = :memberId
        """;
        return client.sql(sql)
                .param("memberId", memberId)
                .query((rs, rowNum) -> new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("imageUrl")
                ))
                .list();
    }
}

