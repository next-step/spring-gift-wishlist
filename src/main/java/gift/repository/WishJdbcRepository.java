package gift.repository;

import gift.domain.Wish;
import gift.dto.CreateWishRequest;
import gift.dto.WishResponse;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WishJdbcRepository implements WishRepository {

    private final JdbcClient client;
    private final SimpleJdbcInsert jdbcInsert;

    public WishJdbcRepository(DataSource dataSource) {
        this.client = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("wish")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Wish save(CreateWishRequest request) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(request);
        Number key = jdbcInsert.executeAndReturnKey(param);
        return new Wish(key.longValue(), request.memberId(), request.productId(), request.quantity());
    }

    @Override
    public List<WishResponse> findAllByMember(Long memberId) {
        String sql = "select wish.id, product.name as productName, product.price, wish.quantity " +
                "from wish " +
                "join product on wish.product_id = product.id " +
                "join member on wish.member_id = member.id " +
                "where member.id = :id";
        return client.sql(sql)
                .param("id", memberId)
                .query(WishResponse.class)
                .list();
    }
}
