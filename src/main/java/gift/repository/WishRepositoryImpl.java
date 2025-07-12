package gift.repository;

import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WishRepositoryImpl implements WishRepository{

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;

    public WishRepositoryImpl(DataSource dataSource) {

        this.jdbcClient = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("wishes")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Wish> findWishList(Long memberId) {

        String sql = "select * from wishes where member_id = ?";

        return jdbcClient
                .sql(sql)
                .param(memberId)
                .query(Wish.class)
                .list();
    }

    @Override
    public Optional<Wish> findWishById(Long wishId) {
        String sql = "select * from wishes where id = ?";

        return jdbcClient
                .sql(sql)
                .param(wishId)
                .query(Wish.class)
                .optional();
    }

    @Override
    public Wish saveWish(Long memberId, Long productId) {

        final Map<String, Object> params = Map.of(
                "member_id", memberId,
                "product_id", productId
        );

        Number key = jdbcInsert.executeAndReturnKey(params);
        Long id = key.longValue();

        return new Wish(id, memberId, productId);
    }

    @Override
    public int deleteWish(Long wishId) {
        String sql = "delete from wishes where id = ?";

        int rowNum = jdbcClient
                .sql(sql)
                .param(wishId)
                .update();

        return rowNum;
    }

    @Override
    public boolean isInWishList(Long memberId, Long productId) {
        String sql = "select * from wishes where member_id = :member_id and product_id = :product_id";

        return jdbcClient
                .sql(sql)
                .param("member_id", memberId)
                .param("product_id", productId)
                .query(Wish.class)
                .optional()
                .isPresent();
    }
}
