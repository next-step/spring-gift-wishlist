package gift.wishlist.repository;

import gift.global.exception.FailedGenerateKeyException;
import gift.global.exception.WishlistNotFoundException;
import gift.wishlist.entity.Wish;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistRepositoryImpl implements WishlistRepository{
    private final JdbcTemplate jdbcTemplate;

    public WishlistRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Wish> findAllByMemberId(Long memberId) {
        final String sql = "SELECT * FROM wishlist WHERE member_id = ?";

        return jdbcTemplate.query(sql, wishListRowMapper(), memberId);
    }

    @Override
    public long save(Wish wish) {
        final String sql = "INSERT INTO wishlist (member_id, product_id, amount) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, wish.getMemberId());
            ps.setLong(2, wish.getProductId());
            ps.setInt(3, wish.getAmount());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new FailedGenerateKeyException();
        }
        return key.longValue();
    }

    @Override
    public void update(Long id, int amount) {
        final String sql = "UPDATE wishlist SET amount = ? WHERE id = ?";

        int updated = jdbcTemplate.update(sql, amount, id);
        if (updated == 0) {
            throw new WishlistNotFoundException();
        }
    }

    @Override
    public void delete(Long memberId, Long productId) {
        final String sql = "DELETE FROM wishlist WHERE member_id = ? AND product_id = ?";

        int deleted = jdbcTemplate.update(sql, memberId, productId);
        if (deleted == 0) {
            throw new WishlistNotFoundException();
        }
    }

    private RowMapper<Wish> wishListRowMapper() {
        return (rs, rowNum) -> new Wish(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getInt("amount")
        );
    }
}
