package gift.repository;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import java.sql.PreparedStatement;
import java.util.List;
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
  public List<ProductResponseDto> findAllProduct() {
    String sql = "select * from products";
    return jdbcTemplate.query(sql, (rs, rowNum) ->
        new ProductResponseDto(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getLong("price"),
            rs.getString("imageUrl")
        )
    );
  }

  @Override
  public ProductResponseDto findProductById(Long id) {
    String sql = "select * from products where id=?";
    return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new ProductResponseDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("price"),
                rs.getString("imageUrl")
            )
        , id);
  }

  @Override
  public ProductResponseDto createProduct(ProductRequestDto requestDto) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    String sql = "insert into products (name,price,imageUrl) values (?, ?, ?)";
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
      ps.setString(1, requestDto.getName());
      ps.setLong(2, requestDto.getPrice());
      ps.setString(3, requestDto.getImageUrl());
      return ps;
    }, keyHolder);

    Long generatedId = keyHolder.getKey().longValue();

    return new ProductResponseDto(generatedId, requestDto.getName(), requestDto.getPrice(),
        requestDto.getImageUrl());
  }

  @Override
  public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
    String sql = "update products set name=?, price=?, imageUrl=? where id=?";
    jdbcTemplate.update(sql, requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl(),
        id);
    return new ProductResponseDto(id, requestDto.getName(), requestDto.getPrice(),
        requestDto.getImageUrl());
  }

  @Override
  public void deleteProduct(Long id) {
    String checkSql = "select count(*) from products where id=?";
    int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
    System.out.println(count);
    if (count != 1) {
      throw new IllegalStateException("삭제할 것이 없습니다");
    }
    String sql = "delete from products where id=?";
    jdbcTemplate.update(sql, id);
  }
}
