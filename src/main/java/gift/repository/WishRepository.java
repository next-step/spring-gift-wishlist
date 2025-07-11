package gift.repository;

import gift.entity.Wish;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepository {

    private final JdbcClient jdbcClient;

    public WishRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    private final RowMapper<Wish> wishRowMapper = (rs, rowNum) -> new Wish(
        rs.getLong("id"),
        rs.getLong("member_id"),
        rs.getLong("product_id")
    );

    public List<Wish> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id FROM wishes WHERE member_id = :memberId";
        return jdbcClient.sql(sql)
            .param("memberId", memberId)
            .query(wishRowMapper)
            .list();
    }

    public Wish save(Wish wish) {
        String sql = "INSERT INTO wishes (member_id, product_id) VALUES (:memberId, :productId)";
        jdbcClient.sql(sql)
            .param("memberId", wish.getMemberId())
            .param("productId", wish.getProductId())
            .update();
        return wish;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM wishes WHERE id = :id";
        jdbcClient.sql(sql)
            .param("id", id)
            .update();
    }
}