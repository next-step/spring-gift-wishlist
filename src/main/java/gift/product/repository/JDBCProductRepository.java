package gift.product.repository;

import gift.product.domain.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JDBCProductRepository implements ProductRepository{

    private final JdbcClient client;

    public JDBCProductRepository(JdbcClient client) {
        this.client = client;
    }

    @Override
    public Product save(String name, int price, String imageUrl) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        client.sql("insert into product (name, price, image_url) values (:name, :price, :image_url)")
                .param("name", name)
                .param("price", price)
                .param("image_url", imageUrl)
                .update(keyHolder);

        Long newId = keyHolder.getKey().longValue();
        return new Product(newId, name, price, imageUrl);
    }

    @Override
    public List<Product> findAll() {
        return client.sql("select id, name, price, image_url from product")
                .query(productRowMapper)
                .list();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return client.sql("select id, name, price, image_url from product where id = :id")
                .param("id", id)
                .query(productRowMapper)
                .optional();
    }

    @Override
    public boolean update(Long id, String name, int price, String imageUrl) {
        int affected = client.sql("update product set name = :name, price = :price, image_url = :imageUrl where id = :id")
                .param("name", name)
                .param("price", price)
                .param("imageUrl", imageUrl)
                .param("id", id)
                .update();

        return affected > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        int affected = client.sql("delete from product where id = :id")
                .param("id", id)
                .update();

        return affected > 0;
    }

    private static final RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );
}
