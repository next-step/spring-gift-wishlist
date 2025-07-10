package gift.wish.repository;

import gift.wish.domain.Wish;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishRepository {
    private final JdbcClient client;

    public WishRepository(JdbcClient client) {
        this.client = client;
    }

    public Wish save(Wish wish) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("insert into wish (member_id, product_id) values (:memberId, :productId)")
                .param("memberId", wish.getMemberId())
                .param("productId", wish.getProductId())
                .update(keyHolder);

        return new Wish(keyHolder.getKey().longValue(), wish.getMemberId(), wish.getProductId());
    }

    public Optional<Wish> findById(Long id) {
        return client.sql("select id, member_id, product_id from wish where id = :id")
                .param("id", id)
                .query(wishRowMapper)
                .optional();
    }

    public List<Wish> findByMemberId(Long memberId) {
        return client.sql("select id, member_id, product_id from wish where member_id = :memberId")
                .param("memberId", memberId)
                .query(wishRowMapper)
                .list();
    }

    public boolean deleteById(Long id) {
        int affected = client.sql("delete from wish where id = :id")
                .param("id", id)
                .update();
        return affected > 0;
    }

    private static final RowMapper<Wish> wishRowMapper = (rs, rowNum) -> new Wish(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id")
    );
}
