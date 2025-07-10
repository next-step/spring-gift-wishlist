package gift.wishlist.mapper;

import gift.product.entity.Product;
import gift.wishlist.entity.WishlistItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class WishlistItemMapper implements RowMapper<WishlistItem> {

    @Override
    public WishlistItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product(
                rs.getLong("product_id"),
                rs.getString("name"),
                rs.getLong("price"),
                rs.getString("image_url"));

        return new WishlistItem(
                UUID.fromString(rs.getString("member_uuid")),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
                rs.getTimestamp("added_at").toLocalDateTime(),
                product);
    }
}
