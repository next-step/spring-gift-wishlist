package gift.repository;

import gift.entity.Wish;
import gift.dto.WishResponseDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WishRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;

    public WishRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("wish")
                .usingGeneratedKeyColumns("id");
    }

    public void save(Long memberId, Long productId) {
        var params = new java.util.HashMap<String, Object>();
        params.put("member_id", memberId);
        params.put("product_id", productId);
        jdbcInsert.execute(params);
    }

    public List<WishResponseDto> findByMemberId(Long memberId) {
        String sql = """
                SELECT p.id AS product_id, p.name AS product_name
                FROM wish w
                JOIN product p ON w.product_id = p.id
                WHERE w.member_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(memberId)
                .query((rs, rowNum) -> new WishResponseDto(
                        rs.getLong("product_id"),
                        rs.getString("product_name")
                ))
                .list();
    }

    public void delete(Long memberId, Long productId) {
        jdbcClient.sql("DELETE FROM wish WHERE member_id = ? AND product_id = ?")
                .params(memberId, productId)
                .update();
    }
}
