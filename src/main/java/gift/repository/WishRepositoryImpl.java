package gift.repository;

import gift.domain.product.MdApprovalStatus;
import gift.dto.WishWithProductDto;
import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class WishRepositoryImpl implements WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Wish saveWish(Wish wish) {
        String sql = "INSERT INTO wishes (member_id, product_id, created_at) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, wish.getMemberId());
            ps.setLong(2, wish.getProductId());
            ps.setTimestamp(3, Timestamp.valueOf(wish.getCreatedAt()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.get("id") != null) {
            Long id = ((Number) keys.get("id")).longValue();
            return wish.withId(id);
        }
        return wish;
    }

    @Override
    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishes WHERE member_id = ? AND product_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }

    public List<WishWithProductDto> findByMemberIdWithProduct(Long memberId) {
        String sql = """
            SELECT w.id AS wish_id, w.member_id, w.product_id, w.created_at,
                   p.name, p.price, p.image_url, p.md_approved
            FROM wishes w
            JOIN products p ON w.product_id = p.id
            WHERE w.member_id = ?
            ORDER BY w.created_at DESC
            """;

        return jdbcTemplate.query(sql, wishWithProductRowMapper, memberId);
    }

    private final RowMapper<WishWithProductDto> wishWithProductRowMapper = new RowMapper<WishWithProductDto>() {
        @Override
        public WishWithProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product(
                    rs.getLong("product_id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getString("image_url"),
                    rs.getBoolean("md_approved") ? MdApprovalStatus.approved() : MdApprovalStatus.notApproved()
            );

            return new WishWithProductDto(
                    rs.getLong("wish_id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    product
            );
        }
    };
}