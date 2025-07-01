package gift.repository;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //repository -> crud동작 수행
    public void add(ProductRequestDto requestDto){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("products").usingGeneratedKeyColumns("id");

        //query문에 들어갈 values
        Map<String, Object> params = new HashMap<>();
        params.put("name", requestDto.getName());
        params.put("price", requestDto.getPrice());
        params.put("image_url", requestDto.getImageUrl());

        simpleJdbcInsert.execute(params);
    }

    public void findById(){

    }

    public void findProducts(){

    }

    public void modifyProduct(){

    }

    public void removeProduct(){

    }

}
