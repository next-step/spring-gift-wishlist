package gift.repository;

import gift.entity.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcClient jdbcClient;

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Long> saveNewProduct(Product product) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into product (name, price, image_url, validated) values (:name, :price, :imageUrl, :validated)")
            .param("name", product.getName())
            .param("price", product.getPrice())
            .param("imageUrl", product.getImageUrl())
            .param("validated", product.getValidated())
            .update(keyHolder);
        return Optional.ofNullable(keyHolder.getKeyAs(Long.class));
    }

    public Optional<Product> getProductById(Long id) {
        return jdbcClient.sql("select * from product where id = :id")
            .param("id", id)
            .query(getProductRowMapper())
            .optional();
    }

    public List<Product> getProductList(Boolean validated) {
        return jdbcClient.sql("select * from product where validated = :validated")
            .param("validated", validated)
            .query(getProductRowMapper())
            .list();
    }

    public boolean updateProduct(Product product) {
        return jdbcClient.sql("update product set name = :name, price = :price, image_url = :imageUrl, validated = :validated where id = :id")
            .param("id", product.getId())
            .param("name", product.getName())
            .param("price", product.getPrice())
            .param("imageUrl", product.getImageUrl())
            .param("validated", product.getValidated())
            .update() == 1;
    }

    public boolean deleteProductById(Long id) {
        return jdbcClient.sql("delete from product where id = :id")
            .param("id", id)
            .update() == 1;
    }

    private static RowMapper<Product> getProductRowMapper() {
        return (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            Integer price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Boolean validated = rs.getBoolean("validated");
            return new Product(id, name, price, imageUrl, validated);
        };
    }
}
