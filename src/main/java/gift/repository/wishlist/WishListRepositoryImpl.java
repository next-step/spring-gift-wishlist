package gift.repository.wishlist;

import gift.entity.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishListRepositoryImpl implements WishListRepository {

    private final JdbcClient jdbcClient;

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
}
