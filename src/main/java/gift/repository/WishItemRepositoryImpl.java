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
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishItemRepositoryImpl(JdbcClient jdbcClient, ProductRepository productRepository,
        MemberRepository memberRepository) {
        this.jdbcClient = jdbcClient;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    private RowMapper<WishItem> getWishItemRowMapper(Member member) {
        return (rs, rowNum) -> {
            Long id = rs.getLong("id");
            Long productId = rs.getLong("productId");
            Integer quantity = rs.getObject("quantity", Integer.class);
            Long memberId = rs.getLong("memberID");
            Product product = this.productRepository.findById(productId)
                .orElseThrow(
                    () -> new WishItemNotFoundException("Product not found for id: " + productId));
            return new WishItem(id, product, quantity, member);
        };
    }

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

        String sql = "SELECT wi.id, p.id AS productId, wi.quantity, wi.memberId FROM wishItems wi JOIN products p ON wi.productId = p.id WHERE wi.productId = ? AND wi.memberId = ?";
        return jdbcClient.sql(sql)
            .param(1, productId)
            .param(2, member.getId())
            .query(getWishItemRowMapper(member))
            .optional();
    }

    @Override
    public void deleteByIdAndMember(Long wishId, Member member) {
        if (wishId == null || member == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }

        String sql = "DELETE FROM wishItems WHERE Id = ? AND memberId = ?";
        int rowsCount = jdbcClient.sql(sql)
            .param(1, wishId)
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

        String sql = "SELECT wi.id, p.id AS productId, wi.quantity, wi.memberId FROM wishItems wi JOIN products p ON wi.productId = p.id WHERE wi.memberId = ?";
        return jdbcClient.sql(sql)
            .param(1, member.getId())
            .query(getWishItemRowMapper(member))
            .list();
    }
}
