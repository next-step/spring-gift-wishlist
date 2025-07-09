package gift.repository;

import gift.domain.Wishlist;
import gift.dto.wishlist.WishlistResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WishlistRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Wishlist save(Wishlist wishlist) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("wishlist").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", wishlist.getUserId());
        params.put("product_id", wishlist.getProductId());

        Long key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params)).longValue();

        return new Wishlist(key, wishlist.getUserId(), wishlist.getProductId());
    }

    public List<WishlistResponse> findAllByUserId(Long id) {
        String sql = """
            select p.id as product_id, p.name as product_name, p.price as product_price 
            from wishlist w 
            join product p on w.product_id = p.id 
            where w.user_id = ?
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new WishlistResponse(
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getString("product_price")
        ), id);
    }
}
