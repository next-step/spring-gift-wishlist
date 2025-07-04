package gift.repository;

import gift.dto.ProductDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcClient jdbc;

    public ProductRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<ProductDto> findAll() {
        return jdbc.sql("SELECT * FROM productDto")
                .query(ProductDto.class)
                .list();
    }

    public Optional<ProductDto> findById(Long id) {
        return jdbc.sql("SELECT * FROM productDto WHERE id= :id")
                .param("id", id)
                .query(ProductDto.class)
                .optional();
    }

    public ProductDto save(ProductDto productDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.sql("""
                    INSERT INTO productDto (name, price, image_url)
                    VALUES (:name, :price, :image_url)
                   """)
                .param("name", productDto.getName())
                .param("price", productDto.getPrice())
                .param("image_url", productDto.getImageUrl())
                .update(keyHolder);
        Long id = keyHolder.getKey().longValue();
        productDto.setId(id);
        return productDto;
    }

    public void update(ProductDto productDto) {
        jdbc.sql("""
                    Update productDto
                    set name = :name, price = :price, image_url = :image_url
                    where id = :id
                """)
                .param("name", productDto.getName())
                .param("price", productDto.getPrice())
                .param("image_url", productDto.getImageUrl())
                .param("id", productDto.getId())
                .update();
    }

    public void delete(Long id) {
        jdbc.sql("DELETE FROM productDto WHERE id = :id")
                .param("id", id)
                .update();
    }
}

