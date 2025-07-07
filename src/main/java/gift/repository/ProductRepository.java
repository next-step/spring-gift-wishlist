package gift.repository;

import gift.entity.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    private final JdbcClient jdbcClient;

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Product addProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO PRODUCTS (name, price, image_url)
                VALUES (:name, :price, :image_url)
                """;

        jdbcClient.sql(sql)
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("image_url", product.getImageUrl())
                .update(keyHolder, "id");

        Long id = keyHolder.getKeyAs(Long.class);
        product.setId(id);
        return product;
    }

    public List<Product> findAllProduct() {
        String sql = """
                SELECT id, name, price, image_url FROM PRODUCTS
                """;

        return jdbcClient.sql(sql)
                .query(productRowMapper())
                .list();
    }

    public Optional<Product> findProductById(Long id) {
        String sql = """
                SELECT id, name, price, image_url FROM PRODUCTS WHERE id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(productRowMapper())
                .optional();
    }

    public Product updateProduct(Product product) {
        String sql = """
                UPDATE PRODUCTS
                SET name = :name,
                    price = :price,
                    image_url = :imageUrl
                WHERE id = :id
                """;

        jdbcClient.sql(sql)
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("imageUrl", product.getImageUrl())
                .param("id", product.getId())
                .update();

        return product;
    }

    public void deleteProduct(Long id) {
        String sql = """
                DELETE FROM PRODUCTS WHERE id = :id
                """;

        jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }

    private RowMapper<Product> productRowMapper() {
        return new RowMapper<Product>() {
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                long price = rs.getLong("price");
                String imageUrl = rs.getString("image_url");
                return new Product(id, name, price, imageUrl);
            }
        };
    }
}
