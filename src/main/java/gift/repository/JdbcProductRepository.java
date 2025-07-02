package gift.repository;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProductResponseDto saveProduct(ProductRequestDto requestDto) {
        String sql = "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, requestDto.getName());
            ps.setInt(2, requestDto.getPrice());
            ps.setString(3, requestDto.getImageUrl());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        return new ProductResponseDto(generatedId, requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
    }

    @Override
    public Optional<ProductResponseDto> findProductById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";

        List<Product> products = jdbcTemplate.query(sql, new Object[]{id}, productRowMapper());

        return products.stream()
                .findFirst()
                .map(ProductResponseDto::new);
    }

    @Override
    public List<ProductResponseDto> findAllProducts() {

        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productRowMapper())
                .stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProduct(Long id, ProductRequestDto requestDto) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql,
                requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl(), id);

        if (updated == 0) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }

    private RowMapper<Product> productRowMapper() {
        return (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url")
        );
    }
}
