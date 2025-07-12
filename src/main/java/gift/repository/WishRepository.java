package gift.repository;

import gift.entity.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepository {
    private final JdbcTemplate jdbcTemplate;
    public WishRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public List<Wish> findUserWishes(Long userId) {
        return jdbcTemplate.query("SELECT id, user_id, product_id, quantity FROM wishes WHERE user_id = ?", wishRowMapper(), userId);
    }

    public Wish addWish(Long userId, Wish wish) {
        jdbcTemplate.update("INSERT INTO wishes(user_id, product_id, quantity) VALUES(?, ?, ?)", userId, wish.productId(), wish.quantity());
        return wish;
    }

    public void updateWish(Long userId, Wish wish) {
        jdbcTemplate.update("UPDATE wishes SET quantity = ? WHERE id = ? AND user_id = ?", wish.quantity(), wish.id(), userId);
    }

    public void deleteWish(Long userId, Wish wish) {
        jdbcTemplate.update("DELETE FROM wishes WHERE id = ? AND user_id = ?", wish.id(), userId);
    }

    // Wishes 조회용 RowMapper
    private RowMapper<Wish> wishRowMapper() {
        return (rs, rowNum) -> new Wish(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("product_id"),
                rs.getLong("quantity")
        );
    }
}
