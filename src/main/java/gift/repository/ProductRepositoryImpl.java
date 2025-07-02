package gift.repository;

import gift.dto.response.ProductGetResponseDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveProduct(Product product) {

        String sql = "INSERT INTO products(name, price, imageUrl) VALUES(?,?,?)";

        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Override
    public List<ProductGetResponseDto> findAllProducts() {

        String sql = "SELECT productId, name, price, imageUrl FROM products";
        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new ProductGetResponseDto(rs.getLong("productId"), rs.getString("name"),
                rs.getDouble("price"), rs.getString("imageUrl")));
    }

    @Override
    public Product findProductById(Long productId) {
        String sql = "SELECT productId, name, price, imageUrl FROM products WHERE productId = ?";

        try {
            Product product = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Product(rs.getLong("productId"), rs.getString("name"),
                    rs.getDouble("price"), rs.getString("imageUrl")), productId);
            return product;
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException("상품이 존재하지 않습니다. productId = " + productId);
        }
    }

    @Override
    public void updateProductById(Product product) {

        String sql = "UPDATE products SET name = ?, price = ?, imageUrl = ? WHERE productId = ?";

        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(),
            product.getProductId());
    }


    @Override
    public void deleteProductById(Long productId) {

        String sql = "DELETE FROM products WHERE productId = ?";

        jdbcTemplate.update(sql, productId);
    }
}