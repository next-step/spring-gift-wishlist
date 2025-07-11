package gift.repository;


import gift.dto.response.WishResponseDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    public List<WishResponseDto> findAllByMemberIdWithProduct(Long memberId) {
        String sql = """
            SELECT 
                p.id AS product_id,
                p.name,
                p.price,
                p.imageUrl,
                w.quantity
            FROM Wish w
            JOIN Product p ON w.product_id = p.id
            WHERE w.member_id = :memberId
        """;

        return jdbcClient.sql(sql)
                .param("memberId", memberId)
                .query((rs, rowNum) -> new WishResponseDto(
                        rs.getLong("product_id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("imageUrl"),
                        rs.getInt("quantity")
                ))
                .list();
    }

}
