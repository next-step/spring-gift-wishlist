package gift.repository;

import gift.domain.WishList;
import gift.dto.WishListResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateWishListRepository implements WishListRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcTemplateWishListRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public WishListResponseDto saveWishList(WishList wishList) {
    String sql = "INSERT INTO wish_list (member_id, product_id, quantity) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, wishList.getMemberId(), wishList.getProductId(), wishList.getQuantity());

    return new WishListResponseDto(
        wishList.getMemberId(),
        wishList.getProductId(),
        wishList.getQuantity()
    );
  }

  @Override
  public Optional<WishListResponseDto> searchByMemberAndProduct(Long memberId, Long productId) {
    String sql = "SELECT member_id, product_id, quantity FROM wish_list WHERE member_id = ? AND product_id = ?";
    try {
      WishListResponseDto dto = jdbcTemplate.queryForObject(sql, wishListResponseRowMapper(), memberId, productId);
      return Optional.of(dto);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<WishListResponseDto> searchAllByMemberId(Long memberId) {
    String sql = "SELECT member_id, product_id, quantity " +
        "FROM wish_list " +
        "WHERE member_id = ? " +
        "ORDER BY member_id ASC, product_id ASC";
    return jdbcTemplate.query(sql, wishListResponseRowMapper(), memberId);
  }

  @Override
  public WishListResponseDto updateQuantity(Long memberId, Long productId, int quantity) {
    String sql = "UPDATE wish_list SET quantity = ? WHERE member_id = ? AND product_id = ?";
    jdbcTemplate.update(sql, quantity, memberId, productId);

    return new WishListResponseDto(memberId, productId, quantity);
  }

  @Override
  public void deleteByMemberAndProduct(Long memberId, Long productId) {
    String sql = "DELETE FROM wish_list WHERE member_id = ? AND product_id = ?";
    jdbcTemplate.update(sql, memberId, productId);
  }

  @Override
  public void deleteAllByMemberId(Long memberId) {
    String sql = "DELETE FROM wish_list WHERE member_id = ?";
    jdbcTemplate.update(sql, memberId);
  }

  private RowMapper<WishListResponseDto> wishListResponseRowMapper() {
    return (rs, rowNum) -> new WishListResponseDto(
        rs.getLong("member_id"),
        rs.getLong("product_id"),
        rs.getInt("quantity")
    );
  }
}
