package gift.repository;

import gift.entity.Product;
import gift.entity.ProductStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    // JdbcClient
    private final JdbcClient client;

    public ProductRepositoryImpl(JdbcClient client) {
        this.client = client;
    }

    @Override
    public Product saveProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        var sql = "INSERT INTO product (name, price, image_url, status) VALUES (:name, :price, :image_url, :status)";
        client.sql(sql)
              .param("name", product.getName())
              .param("price", product.getPrice())
              .param("image_url", product.getImageUrl())
              .param("status", product.getStatus().name())
              .update(keyHolder);

        return new Product(keyHolder.getKey().longValue(), product.getName(), product.getPrice(),
                product.getImageUrl(), product.getStatus());
    }

    @Override
    public Optional<Product> findProduct(Long productId) {
        var sql = "SELECT * FROM product WHERE id = :id";

        return client.sql(sql)
                     .param("id", productId)
                     .query(Product.class)
                     .optional();
    }

    @Override
    public void updateProduct(Product product) {
        var sql = "UPDATE product SET name = :name, price = :price, image_url = :image_url, status = :status  WHERE id = :id";

        client.sql(sql)
              .param("name", product.getName())
              .param("price", product.getPrice())
              .param("image_url", product.getImageUrl())
              .param("status", product.getStatus().name())
              .param("id", product.getId())
              .update();
    }

    @Override
    public void deleteProduct(Long productId) {
        var sql = "DELETE FROM product WHERE id = :id";

        client.sql(sql)
              .param("id", productId)
              .update();
    }

    @Override
    public List<Product> findAllProducts() {
        var sql = "SELECT * FROM product";

        return client.sql(sql)
                     .query(Product.class)
                     .list();
    }

    @Override
    public void updateProductStatus(Long productId, ProductStatus newStatus) {
        var sql = "UPDATE product SET status = :status  WHERE id = :id";

        client.sql(sql)
              .param("status", newStatus.name())
              .param("id", productId)
              .update();
    }
}
