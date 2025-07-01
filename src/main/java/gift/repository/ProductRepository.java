package gift.repository;

import gift.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert productInserter;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.productInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }


    public Product save(String name, int price, String imageUrl) {
        Map<String, Object> params = Map.of(
                "name", name,
                "price", price,
                "image_url", imageUrl,
                "category_id", null
        );

        Number id = productInserter.executeAndReturnKey(new MapSqlParameterSource(params));
        return new Product(id.longValue(), name, price, imageUrl);
    }

    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        List<Product> result = jdbcTemplate.query(sql, rowMapper(), id);
        return result.stream().findAny();
    }

    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", rowMapper());
    }

    private RowMapper<Product> rowMapper() {
        return (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url")
        );
    }

    public boolean deleteById(Long id) {
        int updated = jdbcTemplate.update("DELETE FROM product WHERE id = ?", id);
        return updated > 0;
    }

    public Optional<Product> update(Long id, String name, int price, String imageUrl) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql, name, price, imageUrl, id);
        if (updated == 0) return Optional.empty();
        return findById(id);
    }
}
