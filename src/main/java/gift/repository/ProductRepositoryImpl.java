package gift.repository;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, rowNum) -> new Product(
        rs.getLong("id"), rs.getString("name"), rs.getInt("price"), rs.getString("imageUrl"));

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        String sql = "select * from product";

        List<Product> productList = jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER);
        return productList;
    }

    @Override
    public Long create(ProductRequestDto requestDto) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("product").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", requestDto.name());
        params.put("price", requestDto.price());
        params.put("imageUrl", requestDto.imageUrl());

        Long id = (Long) jdbcInsert.executeAndReturnKey(params);
        return id;
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "select * from product where id = ?";

        try {
            Product product = jdbcTemplate.queryForObject(sql, PRODUCT_ROW_MAPPER, id);
            return Optional.of(product);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int update(Long id, ProductRequestDto requestDto) {
        String sql = "update product set name = ?, price = ?, imageUrl = ? where id = ?";

        int updateRowCount = jdbcTemplate.update(
            sql,
            requestDto.name(),
            requestDto.price(),
            requestDto.imageUrl(),
            id
        );

        return updateRowCount;
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from product where id = ?";

        int deleteRowCount = jdbcTemplate.update(sql, id);

        if (deleteRowCount <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
