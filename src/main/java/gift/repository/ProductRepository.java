package gift.repository;

import gift.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;
    public ProductRepository(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("products");
    }

    public Product save(Product product)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("name", product.getName());
        map.put("price", product.getPrice());
        map.put("image_url", product.getImageUrl());
        Number key= insert.executeAndReturnKey(new MapSqlParameterSource(map));
        product.setId(key.longValue());
        return product;
    }

    public List<Product> findAll()
    {
        String sql = "select * from products";
        return jdbcTemplate.query(sql,PRODUCT_ROW_MAPPER);
    }

    public Optional<Product> findById(Long id)
    {
        String sql = "select * from products where id=?";
        List<Product>res=jdbcTemplate.query(sql,PRODUCT_ROW_MAPPER,id);
        return res.stream().findFirst();
    }

    public void update(Long id,Product product)
    {
        String sql = "update products set name = ?, price = ?, image_url = ? where id = ?";
        jdbcTemplate.update(sql,product.getName(),product.getPrice(),product.getImageUrl(),id);
    }

    public void delete(Long id)
    {
        String sql = "delete from products where id = ?";
        jdbcTemplate.update(sql,id);
    }

    private static final RowMapper<Product> PRODUCT_ROW_MAPPER=(rs, rowNum) -> new Product(
          rs.getLong("id"),
          rs.getString("name"),
          rs.getInt("price"),
          rs.getString("image_url")
        );
}



