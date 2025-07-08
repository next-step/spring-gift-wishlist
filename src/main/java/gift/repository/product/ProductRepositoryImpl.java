package gift.repository.product;

import gift.entity.product.Product;
import gift.exception.ProductNotFoundExection;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private static final String SELECT_BASE = "SELECT id, name, price, image_url, hidden FROM product";
    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;
    private final RowMapper<Product> productRowMapper;

    public ProductRepositoryImpl(JdbcTemplate jdbc, DataSource dataSource,
            ProductRowMapper productRowMapper) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
        this.productRowMapper = productRowMapper;
    }

    @Override
    public List<Product> findAll() {
        return jdbc.query(SELECT_BASE, productRowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        List<Product> list = jdbc.query(
                SELECT_BASE + " WHERE id = ?", productRowMapper, id);
        return list.stream().findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM product WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Product save(Product product) {
        if (product.id() == null || product.id().id() <= 0) {
            return insertProduct(product);
        } else {
            return updateProduct(product);
        }
    }

    private Product insertProduct(Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", product.name().name());
        params.put("price", product.price().price());
        params.put("image_url", product.imageUrl().url());
        params.put("hidden", product.hidden());
        Number key = insert.executeAndReturnKey(params);
        return product.withId(key.longValue());
    }

    private Product updateProduct(Product product) {
        int updated = jdbc.update(
                "UPDATE product SET name = ?, price = ?, image_url = ?, hidden = ? WHERE id = ?",
                product.name().name(),
                product.price().price(),
                product.imageUrl().url(),
                product.hidden(),
                product.id().id()
        );
        if (updated == 0) {
            throw new ProductNotFoundExection(product.id().id());
        }
        return product;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id는 null이거나 0 이하일 수 없습니다.");
        }
        int deleted = jdbc.update("DELETE FROM product WHERE id = ?", id);
        if (deleted == 0) {
            throw new ProductNotFoundExection(id);
        }
    }
}
