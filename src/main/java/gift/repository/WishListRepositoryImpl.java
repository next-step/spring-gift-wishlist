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
}
