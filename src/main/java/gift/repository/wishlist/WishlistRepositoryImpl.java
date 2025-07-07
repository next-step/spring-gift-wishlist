package gift.repository.wishlist;

import gift.entity.WishlistInfo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistRepositoryImpl implements WishlistRepository {
    private final JdbcClient wishlist;
    
    public WishlistRepositoryImpl(JdbcClient wishlist) {
        this.wishlist = wishlist;
    }
    
    @Override
    public WishlistInfo addToMyWishlist(Long memberId, Long productId, Long productCnt) {
        var sql = """
            insert into wishlist(memberId, productId, productCnt)
            values (:memberId, :productId, :productCnt);
            """;
        
        wishlist.sql(sql)
            .param("memberId", memberId)
            .param("productId", productId)
            .param("productCnt", productCnt)
            .update();
        
        return new WishlistInfo(memberId, productId, productCnt);
    }
}
