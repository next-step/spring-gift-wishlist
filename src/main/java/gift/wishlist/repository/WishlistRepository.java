package gift.wishlist.repository;


import gift.wishlist.entity.Wishlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishlistRepository {
    private static final Logger log = LoggerFactory.getLogger(WishlistRepository.class);
    private final JdbcClient jdbcClient;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Wishlist saveWish(Long memberId, Long productId, int quantity) {
        String sql = """
            MERGE INTO wishlist (member_id, product_id, quantity)
            KEY(member_id, product_id)
            VALUES (?, ?, ?)
            """;

        Optional<Wishlist> wish = findWishByMemberIdAndProductId(memberId, productId);
        int newQuantity = wish.map(w -> w.getQuantity() + quantity).orElse(quantity);

        jdbcClient.sql(sql)
                .param(memberId)
                .param(productId)
                .param(newQuantity)
                .update();

        return findWishByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new IllegalStateException("저장 작업 후 데이터를 찾지 못했습니다."));

    }

    public Optional<Wishlist> findWishByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = """
                SELECT id, member_id, product_id, quantity
                FROM wishlist
                WHERE member_id = ?
                AND product_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(memberId)
                .param(productId)
                .query(getRowMapper())
                .optional();
    }

    public List<Wishlist> findAllByMemberId(Long memberId) {
        String sql = """
                SELECT id, member_id, product_id, quantity
                FROM wishlist
                WHERE member_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(memberId)
                .query(getRowMapper())
                .list();
    }

    public void deleteWishByMemberIdAndProductId(Long memberId, Long wishId) {
        String sql = """
                DELETE FROM wishlist
                WHERE id = ?
                AND member_id = ?
                """;

        int affectedRows = jdbcClient.sql(sql)
                .param(wishId)
                .param(memberId)
                .update();

        if(affectedRows == 0){
            throw new IllegalArgumentException("삭제할 위시리스트가 존재하지 않습니다.");
        }
    }

    private static RowMapper<Wishlist> getRowMapper(){
        return (rs, rowNum) -> new Wishlist(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity")
        );
    }
}
