package gift.repository;

import gift.entity.Item;
import gift.exception.ItemNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ItemRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("products")
            .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Item> itemRowMapper = (rs, rowNum) -> new Item(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getInt("price"),
        rs.getString("image_url")
    );

    public Item save(Item item) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", item.getName());
        params.put("price", item.getPrice());
        params.put("image_url", item.getImageUrl());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Item(id, item.getName(), item.getPrice(), item.getImageUrl());
    }

    public Optional<Item> findById(Long id) {
        String sql = "SELECT id, name, price, image_url FROM products WHERE id = :id";
        return jdbcClient.sql(sql)
            .param("id", id)
            .query(itemRowMapper)
            .optional();
    }

    public List<Item> findAll(int page, int size, String sortProperty, String sortDirection) {
        validateSortProperty(sortProperty);

        String sql = String.format(
            "SELECT id, name, price, image_url FROM products ORDER BY %s %s LIMIT :limit OFFSET :offset",
            sortProperty, "desc".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC");

        int offset = page * size;
        return jdbcClient.sql(sql)
            .param("limit", size)
            .param("offset", offset)
            .query(itemRowMapper)
            .list();
    }

    public List<Item> findAllByIdIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        String sql = "SELECT id, name, price, image_url FROM products WHERE id IN (:ids)";
        return jdbcClient.sql(sql)
            .param("ids", ids)
            .query(itemRowMapper)
            .list();
    }

    public void update(Item item) {
        String sql = "UPDATE products SET name = :name, price = :price, image_url = :imageUrl WHERE id = :id";
        int affectedRows = jdbcClient.sql(sql)
            .param("name", item.getName())
            .param("price", item.getPrice())
            .param("imageUrl", item.getImageUrl())
            .param("id", item.getId())
            .update();

        if (affectedRows == 0) {
            throw new ItemNotFoundException("업데이트할 상품을 찾을 수 없습니다: " + item.getId());
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM products WHERE id = :id";
        jdbcClient.sql(sql)
            .param("id", id)
            .update();
    }

    private void validateSortProperty(String property) {
        List<String> allowedProperties = Arrays.asList("id", "name", "price");
        if (!allowedProperties.contains(property)) {
            throw new IllegalArgumentException("유효하지 않은 정렬 기준입니다: " + property);
        }
    }
}