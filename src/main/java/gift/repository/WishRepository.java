package gift.repository;

import gift.entity.WishItem;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WishRepository {

    private final JdbcClient jdbcClient;

    public WishRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private WishItem mapRowToWishItem(ResultSet rs, int rowNum) throws SQLException {
        return new WishItem(
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url"),
                rs.getInt("quantity")
        );
    }

    public List<WishItem> findAllByMemberId(Long memberId) {
        String sql = """
                SELECT * FROM wi.member_id, p.name, p.price, p.image_url, wi.quantity 
                FROM wish_items wi 
                JOIN products p
                ON wi.product_id = p.id
                WHERE wi.member_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(memberId)
                .query(this::mapRowToWishItem)
                .list();
    }

}
