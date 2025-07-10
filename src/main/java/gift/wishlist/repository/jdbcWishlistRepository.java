package gift.wishlist.repository;

import gift.wishlist.dto.WishlistRequestDto;
import gift.wishlist.dto.WishlistResponseDto;
import gift.wishlist.entity.Wishlist;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class jdbcWishlistRepository implements  WishlistRepository {

    private final JdbcTemplate jdbcTemplate;

    public jdbcWishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public WishlistResponseDto addProductToWishlist(Long memberId, WishlistRequestDto requestDto) {

        Long productId = requestDto.productId();
        int quantityToAdd = requestDto.quantity();

        // 이미 담은 상품인지 확인
        String selectSql = "SELECT * FROM wishlist WHERE member_id = ? AND product_id = ?";
        List<Wishlist> exist = jdbcTemplate.query(selectSql, new Object[]{memberId, productId}, wishlistRowMapper());

        if (exist.isEmpty()) {
            // 없는 경우 추가
            String insertSql = "INSERT INTO wishlist (member_id, product_id, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, memberId, productId, quantityToAdd);
        } else {
            // 있으면 수량만 증가
            String updateSql = "UPDATE wishlist SET quantity = quantity + ? WHERE member_id = ? AND product_id = ?";
            jdbcTemplate.update(updateSql, quantityToAdd, memberId, productId);
        }

        String joinSql = """
            SELECT p.id AS product_id, p.name, p.price, p.image_url, w.quantity
            FROM wishlist w
            JOIN product p ON w.product_id = p.id
            WHERE w.member_id = ? AND w.product_id = ?
        """;

        return jdbcTemplate.queryForObject(
            joinSql,
            new Object[]{memberId, productId},
            (rs, rowNum) -> new WishlistResponseDto(
                rs.getLong("product_id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getInt("price"),
                rs.getString("image_url")
            )
        );
    }

    @Override
    public List<WishlistResponseDto> getWishlist(Long memberId) {

        String sql = """
        SELECT 
            p.id AS product_id,
            p.name AS product_name,
            p.price,
            p.image_url,
            w.quantity
        FROM wishlist w
        JOIN product p ON w.product_id = p.id
        WHERE w.member_id = ?
        """;

        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) ->
                new WishlistResponseDto(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getInt("price"),
                        rs.getString("image_url")
                )
        );
    }


    private RowMapper<Wishlist> wishlistRowMapper() {
        return (rs, rowNum) -> new Wishlist(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
