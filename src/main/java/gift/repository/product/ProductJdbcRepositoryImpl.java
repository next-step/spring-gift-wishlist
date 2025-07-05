package gift.repository.product;

import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJdbcRepositoryImpl implements ProductRepository {

  private JdbcTemplate jdbcTemplate;

  public ProductJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Product> findAllProduct() {
    String sql = "select * from products";
    return jdbcTemplate.query(sql, (rs, rowNum) ->
        new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getLong("price"),
            rs.getString("imageUrl")
        )
    );
  }

  @Override
  public Optional<Product> findProductById(Long id) {
    String sql = "select * from products where id=?";
    try {
      Product result = jdbcTemplate.queryForObject(sql,
          (rs, rowNum) -> new Product(
              rs.getLong("id"),
              rs.getString("name"),
              rs.getLong("price"),
              rs.getString("imageUrl"))
          , id);
      return Optional.of(result);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Product createProduct(Product product) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    String sql = "insert into products (name,price,imageUrl) values (?, ?, ?)";
    jdbcTemplate.update((connection) -> {
      PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
      ps.setString(1, product.getName());
      ps.setLong(2, product.getPrice());
      ps.setString(3, product.getImageUrl());
      return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key == null) {
      throw new IllegalStateException("생성된 ID가 존재하지 않습니다.");
    }
    Long generatedId = key.longValue();
    product.setId(generatedId);
    return product;
  }

  @Override
  public Optional<Product> updateProduct(Long id, Product product) {
    String sql = "update products set name=?, price=?, imageUrl=? where id=?";
    int update = jdbcTemplate.update(sql, product.getName(), product.getPrice(),
        product.getImageUrl(),
        id);
    if (update == 0) {
      return Optional.empty();
    }
    product.setId(id);
    return Optional.of(product);
  }

  @Override
  public int deleteProduct(Long id) {
    String sql = "delete from products where id=?";
    int deletedProduct = jdbcTemplate.update(sql, id);
    if (deletedProduct != 1) {
      throw new ProductNotFoundException("삭제할 것이 없습니다");
    }
    return deletedProduct;
  }
}
