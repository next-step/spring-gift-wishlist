package gift.repository.wishlist;

import gift.dto.api.product.ProductResponseDto;
import gift.entity.WishlistInfo;
import gift.exception.notfound.NotInWishlistException;
import java.util.List;
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
    
    @Override
    public List<WishlistInfo> findMyWishlistByUserId(Long id) {
        var sql = """
            select memberId, productId, productCnt from wishlist
            where memberId = :memberId;
            """;
        
        return wishlist.sql(sql)
            .param("memberId", id)
            .query((rs, rowNum) -> new WishlistInfo(
                rs.getLong("memberId"),
                rs.getLong("productId"),
                rs.getLong("productCnt")
            )).list();
    }
    
    @Override
    public WishlistInfo checkMyWishlist(Long memberId, Long productId) {
        var sql = """
            select memberId, productId, productCnt from wishlist
            where memberId = :memberId and productId = :productId;
            """;
        
        return wishlist.sql(sql)
            .param("memberId", memberId)
            .param("productId", productId)
            .query((rs, rowNum) -> new WishlistInfo(
                rs.getLong("memberId"),
                rs.getLong("productId"),
                rs.getLong("productCnt")
            )).optional()
            .orElseThrow(NotInWishlistException::new);
    }
    
    @Override
    public void deleteFromMyWishlist(Long id, Long productId) {
        var sql = """
            delete from wishlist where memberId = :memberId and productId = :productId;
            """;
        
        wishlist.sql(sql)
            .param("memberId", id)
            .param("productId", productId)
            .update();
    }
}
