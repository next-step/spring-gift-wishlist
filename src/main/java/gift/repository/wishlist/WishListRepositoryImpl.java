package gift.repository.wishlist;

import gift.entity.Wish;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishListRepositoryImpl implements WishListRepository {

    private final JdbcClient jdbcClient;
    private static final RowMapper<Wish> WISH_ROW_MAPPER = ((rs, rowNum) -> new Wish(
        rs.getLong("id"), rs.getLong("product_id"), rs.getLong("member_id")));

    public WishListRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public Wish create(Wish wish) {
        String sql = "insert into wishlist(product_id, member_id) values (:productId, :memberId)";

        jdbcClient.sql(sql)
            .param("productId", wish.getProductId())
            .param("memberId", wish.getMemberId())
            .update();

        return wish;
    }

    @Override
    public List<Wish> findAll(Long memberId) {
        String sql = "select id, product_id, member_id from wishlist where member_id = :memberId";

        List<Wish> wishList = jdbcClient.sql(sql)
            .param("memberId", memberId)
            .query(WISH_ROW_MAPPER)
            .list();

        return wishList;
    }
}
