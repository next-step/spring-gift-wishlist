package gift.repository;

import gift.dto.ProductResponseDto;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao implements ProductRepository {

    private final JdbcClient client;
    private final RowMapper<Product> getProductRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Long price = rs.getLong("price");
        String imageUrl = rs.getString("imageUrl");

        return new Product(id, name, price, imageUrl);
    };

    public ProductDao(JdbcClient client) {
        this.client = client;
    }

    @Override
    public Product createProduct(Product newProduct) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into products(name, price, imageUrl) values (:name, :price, :imageUrl);";
        client.sql(sql)
                .param("name", newProduct.getName())
                .param("price", newProduct.getPrice())
                .param("imageUrl", newProduct.getImageUrl())
                .update(keyHolder);

        Product savedProduct = new Product(keyHolder.getKey().longValue(), newProduct.getName(),
                newProduct.getPrice(),
                newProduct.getImageUrl());

        return savedProduct;
    }

    @Override
    public List<Product> findAllProducts() {
        String sql = "select id, name, price, imageUrl from products;";
        return client.sql(sql)
                .query(getProductRowMapper)
                .list();
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        String sql = "select id, name, price, imageUrl from products where id = :id;";
        return client.sql(sql)
                .param("id", id)
                .query(getProductRowMapper)
                .optional();
    }

    @Override
    public void deleteProductById(Long id) {
        String sql = "delete from products where id = :id;";
        client.sql(sql)
                .param("id", id)
                .update();
    }

    @Override
    public Product updateProductById(Long id, Product newProduct) {
        String sql = "update products set name = :name, price = :price, imageUrl = :imageUrl where id = :id;";
        client.sql(sql)
                .param("name", newProduct.getName())
                .param("price", newProduct.getPrice())
                .param("imageUrl", newProduct.getImageUrl())
                .param("id", newProduct.getId())
                .update();

        Product updatedProduct = new Product(id, newProduct.getName(),
                newProduct.getPrice(),
                newProduct.getImageUrl());

        return updatedProduct;
    }
}
