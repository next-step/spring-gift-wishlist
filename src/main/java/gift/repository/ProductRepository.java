package gift.repository;

import gift.dto.product.ProductRequestDto;
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

    public List<ProductRequestDto> findAll() {
        return jdbc.sql("SELECT * FROM product")
                .query(ProductRequestDto.class)
                .list();
    }

    public Optional<ProductRequestDto> findById(Long id) {
        return jdbc.sql("SELECT * FROM product WHERE id= :id")
                .param("id", id)
                .query(ProductRequestDto.class)
                .optional();
    }

    public ProductRequestDto save(ProductRequestDto productRequestDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.sql("""
                    INSERT INTO product (name, price, image_url)
                    VALUES (:name, :price, :image_url)
                   """)
                .param("name", productRequestDto.getName())
                .param("price", productRequestDto.getPrice())
                .param("image_url", productRequestDto.getImageUrl())
                .update(keyHolder);
        Long id = keyHolder.getKey().longValue();
        productRequestDto.setId(id);
        return productRequestDto;
    }

    public void update(ProductRequestDto productRequestDto) {
        jdbc.sql("""
                    Update product
                    set name = :name, price = :price, image_url = :image_url
                    where id = :id
                """)
                .param("name", productRequestDto.getName())
                .param("price", productRequestDto.getPrice())
                .param("image_url", productRequestDto.getImageUrl())
                .param("id", productRequestDto.getId())
                .update();
    }

    public void delete(Long id) {
        jdbc.sql("DELETE FROM product WHERE id = :id")
                .param("id", id)
                .update();
    }
}

