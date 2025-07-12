package gift.repository;

import gift.entity.Wishlist;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class WishlistRepository {
    private final JdbcTemplate jdbcTemplate;

    public WishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addWishList(Wishlist wishlist) {
        String sql = "INSERT INTO Wishlist(gift_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, wishlist.getGiftId(), wishlist.getUserId());
    }

    public List<Wishlist> getWishList(Long userId){
        return jdbcTemplate.query("SELECT * FROM Wishlist where user_id=?",
                (rs, rowNum) -> new Wishlist(
                    rs.getLong("gift_id"),
                    rs.getLong("user_id")
                )
                ,userId
        );
    }

    public void deleteWishList(Wishlist wishlist) {
        String sql = "DELETE FROM Wishlist where gift_id=? and user_id=?";
        jdbcTemplate.update(sql, wishlist.getGiftId(), wishlist.getUserId());
    }
}
