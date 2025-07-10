package gift.repository;

import gift.entity.Wish;
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
        String sql = "insert into wishes(productId, memberEmail, quantity) values (:productId, :memberEmail, :quantity);";
        client.sql(sql)
                .param("productId", newWish.getProductId())
                .param("memberEmail", newWish.getMemberId())
                .param("quantity", newWish.getQuantity())
                .update(keyHolder);

        Wish savedWish = new Wish(keyHolder.getKey().longValue(), newWish.getProductId(),
                newWish.getMemberId(), newWish.getQuantity());

        return savedWish;
    }
}