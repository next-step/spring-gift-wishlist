package gift.repository;

import gift.domain.Product;
import gift.domain.WishList;
import gift.repository.projection.WishListWithProduct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcWishListRepository implements WishListRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcWishListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public WishList save(WishList wishList) {
        Objects.requireNonNull(wishList, "wishList는 null일 수 없습니다.");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO wishlist (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, wishList.memberId());
            ps.setLong(2, wishList.productId());
            ps.setInt(3, wishList.quantity());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("ID 생성에 실패했습니다.");
        }

        wishList.assignId(key.longValue());
        return wishList;
    }

    @Override
    public void update(Long id, WishList updatedWishList) {
        Objects.requireNonNull(id, "id는 null일 수 없습니다.");
        Objects.requireNonNull(updatedWishList, "updatedWishList는 null일 수 없습니다.");

        int updatedRows = jdbcTemplate.update(
                "UPDATE wishlist SET member_id = ?, product_id = ?, quantity = ? WHERE id = ?",
                updatedWishList.memberId(),
                updatedWishList.productId(),
                updatedWishList.quantity(),
                id
        );

        if (updatedRows == 0) {
            throw new IllegalArgumentException("해당 ID에 대한 wishlist가 존재하지 않아 업데이트할 수 없습니다: " + id);
        }
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        jdbcTemplate.update("DELETE FROM wishlist WHERE id IN (" + inSql + ")", ids.toArray());
    }

    @Override
    public int deleteByIdAndMemberId(Long id, Long memberId) {
        return jdbcTemplate.update("DELETE FROM wishlist WHERE id = ? AND member_id = ?", id,
                memberId);
    }

    @Override
    public List<WishList> findAll() {
        return jdbcTemplate.query(
                "SELECT id, member_id, product_id, quantity FROM wishlist",
                this::mapRowToWishList
        );
    }

    @Override
    public Optional<WishList> findById(Long id) {
        try {
            WishList result = jdbcTemplate.queryForObject(
                    "SELECT id, member_id, product_id, quantity FROM wishlist WHERE id = ?",
                    this::mapRowToWishList,
                    id
            );
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<WishListWithProduct> findAllWithProductByMemberId(Long memberId) {
        String sql = """
                SELECT
                    w.id AS wishlist_id,
                    w.member_id,
                    w.quantity,
                    p.id AS product_id,
                    p.name AS product_name,
                    p.price AS product_price,
                    p.image_url AS product_image_url
                FROM wishlist w
                JOIN product p ON w.product_id = p.id
                WHERE w.member_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Product product = new Product(
                    rs.getLong("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("product_price"),
                    rs.getString("product_image_url")
            );

            return new WishListWithProduct(
                    rs.getLong("wishlist_id"),
                    rs.getLong("member_id"),
                    rs.getInt("quantity"),
                    product
            );
        }, memberId);
    }

    @Override
    public Optional<WishList> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wishlist WHERE member_id = ? AND product_id = ?";

        try {
            WishList wishList = jdbcTemplate.queryForObject(sql, this::mapRowToWishList, memberId,
                    productId);
            return Optional.of(wishList);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private WishList mapRowToWishList(ResultSet rs, int rowNum) throws SQLException {
        return new WishList(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity")
        );
    }
}
