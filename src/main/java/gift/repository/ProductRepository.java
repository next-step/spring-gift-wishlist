package gift.repository;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import java.lang.reflect.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //repository -> crud동작 수행
    public Long add(ProductRequestDto requestDto){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("products").usingGeneratedKeyColumns("id");

        //query문에 들어갈 values
        Map<String, Object> params = new HashMap<>();
        params.put("name", requestDto.getName());
        params.put("price", requestDto.getPrice());
        params.put("image_url", requestDto.getImageUrl());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return id;
    }

    public Optional<Product> findById(Long id){
        String sql = "select * from products where id = ?";
        List<Product> productList = jdbcTemplate.query(sql, productRowMapper(), id);
        return productList.stream().findAny();
    }

    public List<Product> findProducts(){
        String sql = "select * from products";
        List<Product> productList = jdbcTemplate.query(sql, productRowMapper());
        return productList;
    }

    public void modifyProduct(Long id, ProductRequestDto requestDto){
        String sql = "update products set name = ?, price =?, image_url = ? where id =?";
        jdbcTemplate.update(sql, requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl(), id);
    }

    public void removeProduct(Long id){
        String sql = "delete from products where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public RowMapper<Product> productRowMapper(){
        return new RowMapper<Product>() {
            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                Integer price = rs.getInt("price");
                String imageUrl = rs.getString("image_url");
                return new Product(id, name, price, imageUrl);
            }
        };
    }

}
