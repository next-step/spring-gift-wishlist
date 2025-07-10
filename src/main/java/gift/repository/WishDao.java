package gift.repository;

import gift.entity.Product;
import gift.entity.Wish;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishDao implements WishRepository{
    private final JdbcClient client;

    private final RowMapper<Wish> getWishRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long productId = rs.getLong("productId");
        Long memberId = rs.getLong("memberId");
        Long quantity = rs.getLong("quantity");
        return new Wish(id, productId, memberId, quantity);
    };

    public WishDao(JdbcClient client) {
        this.client = client;
    }

    @Override
    public Wish createWish(Wish newWish) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into wishes(productId, memberId, quantity) values (:productId, :memberId, :quantity);";
        client.sql(sql)
                .param("productId", newWish.getProductId())
                .param("memberId", newWish.getMemberId())
                .param("quantity", newWish.getQuantity())
                .update(keyHolder);

        Wish savedWish = new Wish(keyHolder.getKey().longValue(), newWish.getProductId(),
                newWish.getMemberId(), newWish.getQuantity());

        return savedWish;
    }

    @Override
    public List<Wish> findMemberWishes(Long memberId) {
        String sql = "select * from wishes where memberId = :memberId;";
        return client.sql(sql)
                .param("memberId", memberId)
                .query(getWishRowMapper)
                .list();
    }

}