package gift.repository;

import gift.entity.Wish;
import java.util.List;
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

    @Override
    public List<Wish> findAllWishes(Long memberId) {
        var sql = "SELECT * FROM wish WHERE member_id = :member_id";

        return client.sql(sql)
                     .param("member_id", memberId)
                     .query(Wish.class)
                     .list();
    }
}
