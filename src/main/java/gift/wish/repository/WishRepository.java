package gift.wish.repository;

import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
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

    public Wish save(Long memberId, Long productId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("insert into wish (member_id, product_id, quantity) values (:memberId, :productId, :quantity)")
                .param("memberId", memberId)
                .param("productId", productId)
                .param("quantity", 1)
                .update(keyHolder);

        return new Wish(keyHolder.getKey().longValue(), memberId, productId, 1);
    }

    public Optional<Wish> findById(Long id) {
        return client.sql("select id, member_id, product_id, quantity from wish where id = :id")
                .param("id", id)
                .query(wishRowMapper)
                .optional();
    }

    public List<WishListResponse> findByMemberId(Long memberId) {
        return client.sql("select " +
                        "w.id as wish_id, " +
                        "w.product_id as product_id, " +
                        "p.name as product_name, " +
                        "p.image_url as product_image_url, " +
                        "w.quantity as quantity " +
                        "from wish as w " +
                        "join product p on w.product_id = p.id" +
                        "where member_id = :memberId")
                .param("memberId", memberId)
                .query(wishListResponseRowMapper)
                .list();
    }

    public boolean updateByIdAndQuantity(Long id, Integer quantity) {
        int affected = client.sql("update wish set quantity = :quantity where id = :id")
                .param("id", id)
                .update();

        return affected > 0;
    }

    public boolean deleteById(Long id) {
        int affected = client.sql("delete from wish where id = :id")
                .param("id", id)
                .update();
        return affected > 0;
    }

    public boolean isExist(Long memberId, Long productId){
        Integer count = client.sql("select count(*) from wish where member_id = :memberId and product_id = :productId")
                .param("memberId", memberId)
                .param("productId", productId)
                .query(Integer.class)
                .single();

        return count > 0;
    }

    private static final RowMapper<Wish> wishRowMapper = (rs, rowNum) -> new Wish(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );

    private static final RowMapper<WishListResponse> wishListResponseRowMapper = (rs, rowNum) -> new WishListResponse(
            rs.getLong("wish_id"),
            rs.getLong("product_id"),
            rs.getString("product_name"),
            rs.getString("product_image_url"),
            rs.getInt("quantity")
    );
}
