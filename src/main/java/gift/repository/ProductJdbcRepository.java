package gift.repository;

import gift.entity.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private RowMapper<Product> productRowMapper() {
        return (rs, rowNum) -> new Product(
            rs.getLong("productId"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("imageURL")
        );
    }


    @Override
    public Optional<Product> findById(long productId) {
        return jdbcTemplate.query("select * from product where productId = ?",
            productRowMapper(),
            productId).stream().findFirst();
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("select * from product", productRowMapper());
    }

    @Override
    public void createProduct(Product product) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("productId", product.productId());
        parameters.put("name", product.name());
        parameters.put("price", product.price());
        parameters.put("imageURL", product.imageURL());

        jdbcInsert.withTableName("product").execute(parameters);
    }

    @Override
    public void updateProduct(Product product) {
        jdbcTemplate.update(
            "update product set name = ?, price = ?, imageURL = ? where productId = ?",
            product.name(), product.price(), product.imageURL(), product.productId());
    }

    @Override
    public void delete(long productId) {
        jdbcTemplate.update("delete from product where productId = ?", productId);
    }

    @Override
    public boolean productExists(long id) {
        return jdbcTemplate.query("select 1 from product where productId = ? limit 1",
                (rs, rowNum) -> 1, id)
            .stream().findFirst().isPresent();
    }
}