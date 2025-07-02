package gift.repository;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
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
  public Optional<ProductResponseDto> findProductById(Long id) {
    String sql = "select * from products where id=?";
    try {
      ProductResponseDto result = jdbcTemplate.queryForObject(sql,
          (rs, rowNum) -> new ProductResponseDto(
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

    ProductResponseDto responseDto = new ProductResponseDto(generatedId, requestDto.getName(),
        requestDto.getPrice(),
        requestDto.getImageUrl());
    return responseDto;
  }

  @Override
  public Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto requestDto) {
    String sql = "update products set name=?, price=?, imageUrl=? where id=?";
    int update = jdbcTemplate.update(sql, requestDto.getName(), requestDto.getPrice(),
        requestDto.getImageUrl(),
        id);
    if (update == 0) {
      return Optional.empty();
    }
    ProductResponseDto responseDto = new ProductResponseDto(id, requestDto.getName(),
        requestDto.getPrice(),
        requestDto.getImageUrl());
    return Optional.of(responseDto);
  }

  @Override
  public int deleteProduct(Long id) {
    String sql = "delete from products where id=?";
    int delete = jdbcTemplate.update(sql, id);
    return delete;
  }
}
