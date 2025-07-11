package gift.repository;

import gift.dto.product.ProductRequestDto;
import gift.model.Product;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcClient jdbc;

    public ProductRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<ProductRequestDto> findAll() {
        return jdbc.sql("SELECT * FROM products")
                .query(ProductRequestDto.class)
                .list();
    }

    public Optional<ProductRequestDto> findById(Long id) {
        return jdbc.sql("SELECT * FROM products WHERE id= :id")
                .param("id", id)
                .query(ProductRequestDto.class)
                .optional();
    }

    public void save(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        jdbc.sql("""
                    INSERT INTO products (name, price, image_url)
                    VALUES (:name, :price, :image_url)
                   """)
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("image_url", product.getImageUrl())
                .update(keyHolder);
        Long id = keyHolder.getKey().longValue();
        product.setId(id);
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
    }

    public void update(ProductRequestDto productRequestDto) {
        jdbc.sql("""
                    Update products
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
        jdbc.sql("DELETE FROM products WHERE id = :id")
                .param("id", id)
                .update();
    }
}

