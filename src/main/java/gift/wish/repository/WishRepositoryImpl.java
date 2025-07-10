package gift.wish.repository;

import gift.member.entity.Member;
import gift.wish.entity.Wish;
import gift.exception.wish.WishNotFoundException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepositoryImpl implements WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addWish(Wish wish) {
        String sql = "INSERT INTO wishes(memberId, productId) VALUES(?,?)";
        jdbcTemplate.update(sql, wish.getMemberId(), wish.getProductId());
    }

    @Override
    public List<Wish> getWishes(Member member, Integer size, Integer offset) {
        String sql = "SELECT wishId, productId, createdDate FROM wishes WHERE memberId = ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql,
            (rs, RowNum) -> new Wish(rs.getLong("wishId"), rs.getLong("productId"),
                rs.getTimestamp("createdDate").toLocalDateTime()), member.getMemberId(), size,
            offset);
    }

    @Override
    public void deleteWish(Long wishId) {
        String sql = "DELETE FROM wishes WHERE wishId = ?";
        jdbcTemplate.update(sql, wishId);
    }

    @Override
    public Wish findByWishId(Long wishId) {
        String sql = "SELECT wishId, memberId, productId, createdDate FROM wishes WHERE wishId = ?";

        try {
            return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Wish(rs.getLong("wishId"), rs.getLong("memberId"),
                    rs.getLong("productId"),
                    rs.getTimestamp("createdDate").toLocalDateTime()), wishId);
        } catch (EmptyResultDataAccessException e) {
            throw new WishNotFoundException("위시 상품이 존재하지 않습니다. wishId = " + wishId);
        }
    }

    @Override
    public Long countWishesByMemberId(Long memberId) {
        String sql = "SELECT COUNT(wishId) FROM wishes WHERE memberId = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, memberId);
    }


}
