package gift.repository;

import gift.domain.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepositoryImpl implements WishRepository {

    private final JdbcClient jdbcClient;

    public WishRepositoryImpl(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Wish add(Wish wish){

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("insert into wish (member_id, product_id) values(:memberId, :productId)")
                .param("memberId", wish.getMemberId())
                .param("productId", wish.getProductId())
                .update(keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Wish(id, wish.getMemberId(), wish.getProductId());
    }

    @Override
    public List<Wish> findAllByMemberId (Long memberId){
        return jdbcClient.sql("select id, product_id from wish where member_id = :memberId")
                .param("memberId", memberId)
                .query((rs, rowNum) ->
                    new Wish(
                        rs.getLong("id"),
                        memberId,
                        rs.getLong("product_id")
                    ))
                .list();
    }
}
