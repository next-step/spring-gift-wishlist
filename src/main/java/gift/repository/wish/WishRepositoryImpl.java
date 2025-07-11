package gift.repository.wish;

import gift.entity.Wish;
import gift.exception.ProductNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepositoryImpl implements WishRepository {

  private JdbcTemplate jdbcTemplate;

  public WishRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Wish> findByMemberId(Long memberId) {
    String sql = "SELECT * FROM WISHLIST WHERE member_id = ?";
    return jdbcTemplate.query(sql, wishRowMapper(), memberId);
  }

  @Override
  public Wish createWish(Wish wish) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String sql = "insert into wishlist (member_id,product_id,quantity) values (?, ?, ?)";
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
  public Optional<Wish> updateQuantity(Long memberId, Wish wish) {
    String sql = "update wishlist set quantity=? where member_id=? and product_id=?";
    int update = jdbcTemplate.update(sql, wish.getQuantity(), wish.getMemberId(),
        wish.getProductId());
    if (update == 0) {
      return Optional.empty();
    }
    String selectSql = "SELECT * FROM wishlist WHERE member_id = ? AND product_id = ?";
    Wish updatedWish = jdbcTemplate.queryForObject(selectSql, wishRowMapper(), wish.getMemberId(),
        wish.getProductId());
    return Optional.of(updatedWish);
  }

  @Override
  public int deleteByMemberId(Long memberId) {
    String sql = "delete from wishlist where member_id=?";
    int deletedProduct = jdbcTemplate.update(sql, memberId);
    if (deletedProduct < 1) {
      throw new ProductNotFoundException("삭제할 것이 없습니다");
    }
    return deletedProduct;
  }

  private static RowMapper<Wish> wishRowMapper() {
    return new RowMapper<Wish>() {
      @Override
      public Wish mapRow(ResultSet rs, int rowNum) throws SQLException {
        Wish wish = new Wish(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getLong("quantity")
        );
        return wish;
      }
    };
  }
}
