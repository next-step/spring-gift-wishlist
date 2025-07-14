package gift.repository;

import gift.entity.WishList;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishListRepositoryImpl implements WishListRepository {

    private final JdbcClient jdbcClient;

    public WishListRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public List<WishList> getWishListByMemberId(Long memberId) {
        String sql = "select id, member_id, product_id, quantity from wish_list where member_id = ?";
        List<WishList> wishLists = jdbcClient.sql(sql).param(memberId).query(WishList.class).list();
        return wishLists;
    }

    @Override
    public void addWishList(Long memberId ,Long productId, Integer quantity) {
        String sql = "insert into wish_list (member_id, product_id, quantity) values (?, ?, ?)";
        jdbcClient.sql(sql).param(memberId).param(productId).param(quantity).update();
    }

    @Override
    public boolean isWishListExistByMemberIdAndWishListId(Long memberId, Long wishListId) {
        String sql = "select count(*) from wish_list where member_id = ? and id = ?";

        return jdbcClient.sql(sql)
                .param(memberId)
                .param(wishListId)
                .query(Long.class)
                .single() > 0L;
    }

    @Override
    public void deleteWishList(Long wishListId) {
        String sql = "delete from wish_list where id = ?";
        jdbcClient.sql(sql).param(wishListId).update();
    }
}
