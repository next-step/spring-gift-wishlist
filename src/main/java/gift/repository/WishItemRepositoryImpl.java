package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishItem;
import gift.exception.InvalidFieldException;
import gift.exception.WishItemNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishItemRepositoryImpl implements WishItemRepository {

    private final JdbcClient jdbcClient;

    public WishItemRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final RowMapper<WishItem> WISHITEM_ROW_MAPPER = (rs, rowNum) -> new WishItem(
        rs.getLong("id"),
        rs.getObject("product", Product.class),
        rs.getInt("quantity"),
        rs.getObject("member", Member.class)
    );

    @Override
    public WishItem save(WishItem wishItem) {
        if (wishItem == null) {
            throw new IllegalArgumentException("WishItem cannot be null");
        }

        if (wishItem.getMember() == null || wishItem.getProduct() == null
            || wishItem.getQuantity() == null) {
            throw new InvalidFieldException("Required fields are missing");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO wishItems (productId, quantity, memberId) VALUES (?, ?, ?)";
        jdbcClient.sql(sql)
            .param(1, wishItem.getProduct().getId())
            .param(2, wishItem.getQuantity())
            .param(3, wishItem.getMember().getId())
            .update(keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalArgumentException("Failed to get id");
        }
        Long id = key.longValue();

        return new WishItem(
            id,
            wishItem.getProduct(),
            wishItem.getQuantity(),
            wishItem.getMember()
        );
    }

    @Override
    public Optional<WishItem> findByIdAndMember(Long productId, Member member) {
        if (productId == null || member == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }

        String sql = "SELECT wi.id, p.id AS productId, p.name, wi.quantity, wi.memberId FROM wishItems wi JOIN products p ON wi.productId = p.id WHERE wi.productId = ? AND wi.memberId = ?";
        return jdbcClient.sql(sql)
            .param(1, productId)
            .param(2, member.getId())
            .query(WishItem.class)
            .optional();
    }

    @Override
    public void deleteByItemAndMember(Long productId, Member member) {
        if (productId == null || member == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }

        String sql = "DELETE FROM wishItems WHERE productId = ? AND memberId = ?";
        int rowsCount = jdbcClient.sql(sql)
            .param(1, productId)
            .param(2, member.getId())
            .update();
        if (rowsCount == 0) {
            throw new WishItemNotFoundException("WishItem not found");
        }
    }

    @Override
    public List<WishItem> findByMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        String sql = "SELECT wi.id, p.id AS productId, p.name, wi.quantity, wi.memberId FROM wishItems wi JOIN products p ON wi.productId = p.id WHERE wi.memberId = ?";
        return jdbcClient.sql(sql)
            .param(1, member.getId())
            .query(WishItem.class)
            .list();
    }
}
