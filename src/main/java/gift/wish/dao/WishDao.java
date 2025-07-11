package gift.wish.dao;

import gift.wish.dto.WishResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishDao {
  private final JdbcClient jdbcClient;

  public WishDao(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public WishResponseDto createWish(Long memberId, Long productId) {
    String sql = "INSERT INTO wishes (memberId, productId) VALUES (?, ?)";

    jdbcClient.sql(sql)
        .param(memberId)
        .param(productId)
        .update();

    String selectSql = "SELECT wishId, memberId, productId " +
        "FROM wishes WHERE memberId = ? AND productId = ?";

    return jdbcClient.sql(selectSql)
        .param(memberId)
        .param(productId)
        .query((rs, rowNum) -> new WishResponseDto(
            rs.getLong("wishId"),
            rs.getLong("memberId"),
            rs.getLong("productId")
        ))
        .single();
  }
}
