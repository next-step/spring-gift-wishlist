package gift.repository;

import gift.dto.UserInfoRequestDto;
import gift.entity.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepository {
    private final JdbcTemplate jdbcTemplate;
    public WishRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public List<Wish> findUserWishes(UserInfoRequestDto userInfoRequestDto) {
        return jdbcTemplate.query("SELECT id, user_id, product_id, quantity FROM wishes WHERE user_id = ?", wishRowMapper(), userInfoRequestDto.id());
    }

    public Wish addWish(Wish wish) {
        jdbcTemplate.update("INSERT INTO wishes(id, user_id, product_id, quantity) VALUES(?, ?, ?)", wish.userId(), wish.productId(), wish.quantity());
        return wish;
    }

    public void updateWish(Wish wish) {
        jdbcTemplate.update("UPDATE wishes SET quantity = ? WHERE id = ?", wish.quantity(), wish.id());
    }

    public void deleteWish(Wish wish) {
        jdbcTemplate.update("DELETE FROM wishes WHERE id = ?", wish.id());
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
