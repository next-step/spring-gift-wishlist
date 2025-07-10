package gift.product.repository;

import gift.product.entity.Product;
import gift.exception.product.ProductNotFoundException;
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

        String sql = "INSERT INTO products(name, price, imageUrl, mdConfirmed) VALUES(?,?,?,?)";

        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(),
            product.getMdConfirmed());
    }

    @Override
    public List<Product> findAllProducts() {

        String sql = "SELECT productId, name, price, imageUrl, mdConfirmed FROM products";

        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Product(rs.getLong("productId"), rs.getString("name"),
                rs.getDouble("price"), rs.getString("imageUrl"), rs.getBoolean("mdConfirmed")));
    }

    @Override
    public Product findProductById(Long productId) {

        String sql = "SELECT productId, name, price, imageUrl, mdConfirmed FROM products WHERE productId = ?";

        try {
            return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Product(rs.getLong("productId"), rs.getString("name"),
                    rs.getDouble("price"), rs.getString("imageUrl"), rs.getBoolean("mdConfirmed")),
                productId);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException("상품이 존재하지 않습니다. productId = " + productId);
        }
    }

    @Override
    public void updateProduct(Product product) {

        String sql = "UPDATE products SET name = ?, price = ?, imageUrl = ?, mdConfirmed = ? WHERE productId = ?";

        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(),
            product.getMdConfirmed(),
            product.getProductId());
    }


    @Override
    public void deleteProduct(Long productId) {

        String sql = "DELETE FROM products WHERE productId = ?";

        jdbcTemplate.update(sql, productId);
    }
}