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

    private static final int NO_ROW_UPDATED = 0;

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
                "image_url", imageUrl
        );

        Number id = productInserter.executeAndReturnKey(new MapSqlParameterSource(params));
        return Product.of(id.longValue(), name, price, imageUrl);
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
        return (rs, rowNum) -> Product.of(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url")
        );
    }

    public boolean deleteById(Long id) {
        int updated = jdbcTemplate.update("DELETE FROM product WHERE id = ?", id);
        return updated > NO_ROW_UPDATED;
    }

    public int update(Long id, String name, int price, String imageUrl) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        return jdbcTemplate.update(sql, name, price, imageUrl, id);
    }
}
