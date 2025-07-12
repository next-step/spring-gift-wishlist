package gift.repository;


import gift.dto.response.WishResponseDto;
import gift.entity.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
            w.id AS wish_id,
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
                        rs.getLong("wish_id"),
                        rs.getLong("product_id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("imageUrl"),
                        rs.getInt("quantity")
                ))
                .list();
    }

    public void remove(Long wishId){
        var sql="DELETE FROM Wish WHERE id = :wishId";

        jdbcClient.sql(sql)
                .param("wishId", wishId)
                .update();
    }

    public Optional<Wish> findById(Long wishId) {
        var sql = "SELECT * FROM Wish WHERE id = :wishId";

        return jdbcClient.sql(sql)
                .param("wishId", wishId)
                .query((rs, rowNum) -> new Wish(
                        rs.getLong("id"),
                        rs.getLong("member_id"),
                        rs.getLong("product_id"),
                        rs.getInt("quantity")
                ))
                .optional();
    }

    public void update(Wish wish) {
        var sql ="UPDATE Wish SET quantity = :quantity WHERE id = :wishId";
        jdbcClient.sql(sql)
                .param("quantity", wish.getQuantity())
                .param("wishId", wish.getId())
                .update();
    }

}
