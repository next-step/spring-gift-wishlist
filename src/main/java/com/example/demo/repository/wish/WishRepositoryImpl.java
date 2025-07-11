package com.example.demo.repository.wish;


import com.example.demo.dto.wish.WishResponseDto;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepositoryImpl implements WishRepository{
  private final JdbcClient jdbcClient;

  public WishRepositoryImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public void saveWishProduct(Long userId, Long productId) {
    String sql = "INSERT INTO wish(user_id, product_id) VALUES(:userId, :productId)";
    jdbcClient.sql(sql)
              .param("userId", userId)
              .param("productId", productId)
              .update();
  }

  @Override
  public boolean existProductById(Long productId) {
    String sql = "SELECT COUNT(*) FROM product WHERE id = :productId";
    Long count = jdbcClient.sql(sql)
                           .param("productId", productId)
                           .query(Long.class)
                           .single();
    return count > 0;
  }

  @Override
  public boolean existUserById(Long userId) {
    String sql = "SELECT COUNT(*) FROM users WHERE id = :userId";
    Long count = jdbcClient.sql(sql)
                           .param("userId", userId)
                           .query(Long.class)
                           .single();
    return count > 0;
  }

  public void deleteWishProduct(Long userId, Long productId){
    String sql = "DELETE FROM wish WHERE user_id = :userId AND product_id = :productId;";
    jdbcClient.sql(sql)
        .param("userId", userId)
        .param("productId", productId)
        .update();
  }

  public List<WishResponseDto> getWishProductList(Long userId) {
    String sql = """
    SELECT p.name, p.price, p.image_url AS imageUrl
    FROM product p
    INNER JOIN wish w ON p.id = w.product_id
    WHERE w.user_id = :userId
""";

    return jdbcClient.sql(sql)
                     .param("userId", userId)
                     .query((rs, rowNum) -> new WishResponseDto(
                         rs.getString("name"),
                         rs.getInt("price"),
                         rs.getString("imageUrl")
                     )).list();
  }

  @Override
  public boolean existWish(Long userId, Long productId) {
    String sql = "SELECT COUNT(*) FROM wish WHERE user_id = :userId AND product_id = :productId";
    Long count = jdbcClient.sql(sql)
        .param("userId", userId)
        .param("productId", productId)
        .query(Long.class)
        .single();
    return count > 0;
  }
}
