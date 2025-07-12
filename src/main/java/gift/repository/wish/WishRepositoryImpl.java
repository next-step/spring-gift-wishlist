package gift.repository.wish;

import gift.entity.Wish;
import gift.exception.notfound.WishListNotFoundException;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepositoryImpl implements WishRepository {

  private JdbcTemplate jdbcTemplate;

  private static final RowMapper<Wish> wishRowMapper = (rs, rowNum) ->
      new Wish(
          rs.getLong("id"),
          rs.getLong("member_id"),
          rs.getLong("product_id"),
          rs.getLong("quantity")
      );

  public WishRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Wish> findByMemberId(Long memberId) {
    String sql = "select * from wishes where member_id = ?";
    return jdbcTemplate.query(sql, wishRowMapper, memberId);
  }

  @Override
  public Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId) {
    String sql = "select * from wishes where member_id = ? and product_id=?";
    try {
      Wish wish = jdbcTemplate.queryForObject(sql, wishRowMapper, memberId, productId);
      return Optional.of(wish);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Wish createWish(Wish wish) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String sql = "insert into wishes (member_id,product_id,quantity) values (?, ?, ?)";
    jdbcTemplate.update((connection) -> {
      PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
      ps.setLong(1, wish.getMemberId());
      ps.setLong(2, wish.getProductId());
      ps.setLong(3, wish.getQuantity());
      return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key == null) {
      throw new IllegalStateException("생성된 ID가 존재하지 않습니다.");
    }
    Long generatedId = key.longValue();
    wish.setId(generatedId);
    return wish;
  }

  @Override
  public int updateQuantity(Long memberId, Wish wish) {
    String sql = "update wishes set quantity=? where member_id=? and product_id=?";
    int updateCount = jdbcTemplate.update(sql, wish.getQuantity(), wish.getMemberId(),
        wish.getProductId());
    if (updateCount < 1) {
      throw new WishListNotFoundException("수량을 수정할 위시리스트가 없습니다");
    }
    return updateCount;
  }

  @Override
  public int deleteByMemberId(Long memberId) {
    String sql = "delete from wishes where member_id=?";
    int deleteCount = jdbcTemplate.update(sql, memberId);
    if (deleteCount < 1) {
      throw new WishListNotFoundException("삭제할 위시리스트가 없습니다");
    }
    return deleteCount;
  }
}
