package gift.repository;

import gift.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("jdbcProductRepository")
public class JdbcProductRepository implements ProductRepositoryInterface{

    private JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product addProduct(Product product) {
        String sql = "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl());

        return product;
    }

    @Override
    public List<Product> findAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, this::mapRowToProduct);
    }

    @Override
    public List<Product> findProductsByPage(int offset, int limit) {
        String sql = "SELECT * FROM product ORDER BY id LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, this::mapRowToProduct, limit, offset);
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";

        try {
            Product product = jdbcTemplate.queryForObject(sql, this::mapRowToProduct, id);
            return Optional.of(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, imageUrl = ? WHERE id = ?";

        int affectedRows = jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), id);
        if (affectedRows > 0) {
            Product updatedProduct = new Product(id, product.getName(), product.getPrice(), product.getImageUrl());
            return Optional.of(updatedProduct);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteProduct(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }

    @Override
    public int countAllProducts() {
        String sql = "SELECT COUNT(*) FROM product";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    public boolean isApprovedKakao(String name) {
        String sql = "SELECT COUNT(*) FROM kakao_product WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("imageUrl")
        );
        return product;
    }


}
