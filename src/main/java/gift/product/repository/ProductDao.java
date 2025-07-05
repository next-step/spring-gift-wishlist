package gift.product.repository;

import gift.product.domain.Product;
import gift.product.dto.RequestDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class ProductDao {
    private final JdbcClient jdbcClient;

    public ProductDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Product save(Product product) {
        jdbcClient.sql("INSERT INTO PRODUCTS (id, name, price, imageUrl) VALUES (:id, :name, :price, :imageUrl)")
                .param("id", product.getId())
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("imageUrl", product.getImageUrl())
                .update();

        return product;
    }


    public List<Product> findAll() {
        return jdbcClient.sql("SELECT * FROM PRODUCTS")
                .query(Product.class)
                .list();
    }

    public Product findById(UUID id) throws EmptyResultDataAccessException {
        return jdbcClient.sql("SELECT * FROM PRODUCTS WHERE id = :id")
                .param("id", id)
                .query(Product.class)
                .single();
    }

    @Transactional
    public Product update(UUID id, RequestDto requestDto) {
        if (requestDto.getName() != null) {
            jdbcClient.sql("UPDATE PRODUCTS SET name = :name WHERE id = :id")
                    .param("name", requestDto.getName())
                    .param("id", id)
                    .update();
        }
        if (requestDto.getPrice() != 0) {
            jdbcClient.sql("UPDATE PRODUCTS SET price = :price WHERE id = :id")
                    .param("price", requestDto.getPrice())
                    .param("id", id)
                    .update();
        }
        if (requestDto.getImageUrl() != null) {
            jdbcClient.sql("UPDATE PRODUCTS SET imageUrl = :imageUrl WHERE id = :id")
                    .param("imageUrl", requestDto.getImageUrl())
                    .param("id", id)
                    .update();
        }

        return jdbcClient.sql("SELECT * FROM PRODUCTS WHERE id = :id")
                .param("id", id)
                .query(Product.class)
                .single();
    }

    public void delete(UUID id) {
        jdbcClient.sql("DELETE FROM PRODUCTS WHERE id = :id")
                .param("id", id)
                .update();
    }

}
