package gift.repository;

import gift.domain.WishSummary;
import gift.domain.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WishListJdbcRepository implements WishListRepository {
    private final JdbcTemplate jdbcTemplate;

    public WishListJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveWish(Wish wish) {
        String sql = "insert into wishlist (member_id, product_id) values (?, ?)";
        jdbcTemplate.update(sql, wish.getMemberId(), wish.getProductId());
    }

    @Override
    public List<WishSummary> findAllWishSummaryByMemberId(Long memberId) {
        String sql = """
                select p.name as product_name, count(*) as count
                from wishlist w
                join product p on w.product_id = p.id
                where w.member_id = 1
                group by p.name, w.product_id;
                """;

        return jdbcTemplate.query(sql, wishSummaryRowMapper(), memberId);
    }

    @Override
    public void deleteWish(Wish wish) {
        String sql = """
                delete
                from wishlist
                where member_id = ? and product_id = ?
                """;

        jdbcTemplate.update(sql, wish.getMemberId(), wish.getProductId());
    }

    private RowMapper<WishSummary> wishSummaryRowMapper() {
        return new RowMapper<WishSummary>() {
            @Override
            public WishSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
                return WishSummary.of(
                        rs.getString("product_name"),
                        rs.getInt("count")
                );
            }
        };
    }
}
