package gift.repository;

import gift.entity.WishList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishlistRepository {
    private final JdbcTemplate jdbcTemplate;
    public WishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public WishList save(WishList wishlist) {
        String sql = "INSERT INTO wishlist(member_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, wishlist.getMemberId(), wishlist.getProductId(), wishlist.getQuantity());
        return wishlist;
    }

    public WishList findById(Integer id) {
        String sql = "SELECT * FROM wishlist WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rn) ->
                new WishList(
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ), id);
    }

    public List<WishList> findByMemberId(Integer memberId) {
        String sql = "SELECT * FROM wishlist WHERE member_id = ?";
        return jdbcTemplate.query(sql, (rs, rn) ->
                new WishList(
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ), memberId);
    }

    public Optional<WishList> findByMemberIdAndProductId(Integer memberId, Integer productId) {
        String sql = "SELECT * FROM wishlist WHERE member_id = ? AND product_id = ?";
        List<WishList> result = jdbcTemplate.query(sql, (rs, rn) ->
                new WishList(
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ), memberId, productId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    public int updateQuantity(Integer id, Integer quantity) {
        String sql = "UPDATE wishlist SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, quantity, id);
    }

    public int deleteById(Integer id) {
        String sql = "DELETE FROM wishlist WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
