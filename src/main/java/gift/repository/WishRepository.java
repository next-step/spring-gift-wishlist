package gift.repository;

import gift.domain.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class WishRepository {
    private final JdbcTemplate jdbc;

    public WishRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Wish> ROW_MAPPER = (rs, rowNum) -> new Wish(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );

    public List<Wish> findByMemberId(Long memberId) {
        return jdbc.query("SELECT * FROM wishes WHERE member_id = ?", ROW_MAPPER, memberId);
    }

    public void save(Wish wish) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO wishes(member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, wish.getMemberId());
            ps.setLong(2, wish.getProductId());
            ps.setInt(3, wish.getQuantity());
            return ps;
        }, kh);
        wish.setId(kh.getKey().longValue());
    }

    public void update(Wish wish) {
        jdbc.update("UPDATE wishes SET quantity = ? WHERE id = ?", wish.getQuantity(), wish.getId());
    }

    public boolean deleteByMemberAndProduct(Long memberId, Long productId) {
        return jdbc.update("DELETE FROM wishes WHERE member_id = ? AND product_id = ?", memberId, productId) > 0;
    }
}
