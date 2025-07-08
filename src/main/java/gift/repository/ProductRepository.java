package gift.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import gift.domain.Product;

@Repository
public class ProductRepository {

    private final JdbcClient jdbcClient;
    private final RowMapper<Product> rowMapper;

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.rowMapper = new DataClassRowMapper<>(Product.class);
    }

    public List<Product> findAll() {
        var sql = """
        SELECT id, name, price, imageUrl
        FROM product
        """;

        return jdbcClient.sql(sql)
            .query(rowMapper)
            .list();
    }

    public Optional<Product> findById(Long id) {
        var sql = """
        SELECT id, name, price, imageUrl
        FROM product
        WHERE id = :id
        """;

        return jdbcClient.sql(sql)
            .param("id", id)
            .query(rowMapper)
            .optional();
    }

    public boolean existsById(Long id) {
        var sql = "SELECT COUNT(*) FROM product WHERE id = :id";

        return jdbcClient.sql(sql)
            .param("id", id)
            .query(Long.class)
            .single() > 0;
    }

    public Long save(Product product) {
        var sql = """
        INSERT INTO product (name, price, imageUrl)
        VALUES (:name, :price, :imageUrl)
        """;
        var keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
            .param("name", product.getName())
            .param("price", product.getPrice())
            .param("imageUrl", product.getImageUrl())
            .update(keyHolder);

        if (keyHolder.getKey() == null) {
            return -1L;
        }

        return keyHolder.getKey().longValue();
    }

    public int update(Product product) {
        var sql = """
        UPDATE product
        SET name = :name, price = :price, imageUrl = :imageUrl
        WHERE id = :id
        """;

        return jdbcClient.sql(sql)
            .param("id", product.getId())
            .param("name", product.getName())
            .param("price", product.getPrice())
            .param("imageUrl", product.getImageUrl())
            .update();
    }

    public int delete(Long id) {
        var sql = "DELETE FROM product WHERE id = :id";

        return jdbcClient.sql(sql)
            .param("id", id)
            .update();
    }
}
