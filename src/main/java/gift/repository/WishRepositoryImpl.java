package gift.repository;

import gift.entity.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepositoryImpl implements WishRepository {

    // JdbcClient
    private final JdbcClient client;

    public WishRepositoryImpl(JdbcClient client) {
        this.client = client;
    }

    @Override
    public Long saveWish(Wish wish) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        var sql = "INSERT INTO wish (member_id, product_id) VALUES (:member_id, :product_id)";
        client.sql(sql)
              .param("member_id", wish.getMemberId())
              .param("product_id", wish.getProductId())
              .update(keyHolder);

        return keyHolder.getKey().longValue();
    }
}
