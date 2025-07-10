package gift.wish.repository;

import gift.wish.entity.Wish;
import gift.wish.exception.WishNotFoundException;
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


}
