package gift.repository;

import gift.entity.Wish;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepository {
    private final JdbcClient jdbcClient;

    public WishRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Wish> findByMemberId(Long memberId) {
        String sql = """
            SELECT id, member_id, product_id FROM WISHLIST WHERE member_id = :id
            """;
        return jdbcClient.sql(sql)
                .param("id", memberId)
                .query(wishRowMapper())
                .list();
    }

    private RowMapper<Wish> wishRowMapper() {
        return (rs, rowNum) -> new Wish(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id")
                );
    }
}
