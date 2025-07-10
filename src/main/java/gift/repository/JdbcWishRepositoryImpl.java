package gift.repository;

import gift.domain.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcWishRepositoryImpl implements WishRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert wishInserter;

    public JdbcWishRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.wishInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("wish")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<Wish> rowMapper() {
        return (rs, rowNum) -> Wish.of(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getTimestamp("created_date").toLocalDateTime()
        );
    }

    // 위시리스트에 상품 추가
    @Override
    public Wish addWish(Long memberId, Long productId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("product_id", productId);

        Number id = wishInserter.executeAndReturnKey(params);

        String sql = "SELECT * FROM wish WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id.longValue());
    }


    // wishId 로 Wish 조회
    @Override
    public Optional<Wish> findById(Long wishId) {
        String sql = "SELECT * FROM wish WHERE id = ?";
        List<Wish> result = jdbcTemplate.query(sql, rowMapper(), wishId);
        return result.stream().findAny();
    }

    // 위시리스트에서 상품 삭제
    @Override
    public void removeByMemberIdAndWishId(Long memberId, Long wishId) {
        String sql = "DELETE FROM wish WHERE id = ? AND member_id = ?";
        jdbcTemplate.update(sql, wishId, memberId);
    }


    // 사용자별 위시리스트 조회
    @Override
    public List<Wish> getWishlistByMemberId(Long memberId) {
        String sql = "SELECT * FROM wish WHERE member_id = ? ORDER BY created_date DESC";
        return jdbcTemplate.query(sql, rowMapper(), memberId);
    }

    // 중복 찜 확인
    @Override
    public boolean isWished(Long memberId, Long productId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM wish WHERE member_id = ? AND product_id = ?)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, memberId, productId);
        return Boolean.TRUE.equals(result);
    }
}
