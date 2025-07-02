package gift.repository;

import gift.entity.Product;
import gift.exception.NotFoundByIdException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DatabaseProductRepository implements ProductRepository {

    private final JdbcClient jdbcClient;

    private static final String SAVE_PRODUCT = """
                INSERT INTO products(name, price, imageUrl, status)
                VALUES (:name, :price, :imageUrl, :status);
        """;
    private static final String SELECT_PRODUCT_BY_ID = """
                SELECT id, name, price, imageUrl, status
                FROM products
                WHERE id = :id;
            """;
    private static final String DELETE_PRODUCT_BY_ID = """
                DELETE FROM products
                WHERE id = :id;
            """;
    private static final String UPDATE_PRODUCT = """
                UPDATE products
                SET name = :name, price = :price, imageUrl = :imageUrl, status = :status
                WHERE id = :id;
            """;
    private static final String SELECT_ALL_PRODUCTS = """
                SELECT id, name, price, imageUrl, status
                FROM products;
            """;

    private static final String UPDATE_STATUS = """
                UPDATE products
                SET status = :status
                WHERE id = :id;
            """;

    DatabaseProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void updateStatus(long id, Product.Status status) {
        int numOfUpdatedRows = jdbcClient.sql(UPDATE_STATUS)
                .param("status", status.name())
                .param("id", id)
                .update();

        if (numOfUpdatedRows == 0)
            throw new NotFoundByIdException("Not Found id: " + id);
    }

    @Override
    public Long saveProduct(String name, Integer price, String imageUrl, Product.Status status) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(SAVE_PRODUCT)
                .param("name", name)
                .param("price", price)
                .param("imageUrl", imageUrl)
                .param("status", status.name())
                .update(generatedKeyHolder);
        return generatedKeyHolder.getKey().longValue();
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return jdbcClient.sql(SELECT_PRODUCT_BY_ID)
                .param("id", id)
                .query((rs, rowNum) -> new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("imageUrl"),
                        Product.Status.valueOf(rs.getString("status")))).optional();
    }

    @Override
    public void deleteProductById(Long id) {
        jdbcClient.sql(DELETE_PRODUCT_BY_ID)
                .param("id", id)
                .update();
    }

    @Override
    public void updateProduct(Product product) {
        int numOfUpdatedRows = jdbcClient.sql(UPDATE_PRODUCT)
                .param("id", product.id())
                .param("name", product.name())
                .param("price", product.price())
                .param("imageUrl", product.imageUrl())
                .param("status", product.status().name())
                .update();
        if (numOfUpdatedRows == 0)
            throw new NotFoundByIdException("Not Found id: " + product.id());
    }

    @Override
    public List<Product> findAllProducts() {
        return jdbcClient.sql(SELECT_ALL_PRODUCTS)
                .query((rs, rowNum) -> new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("imageUrl"),
                        Product.Status.valueOf(rs.getString("status"))
                )).stream().toList();
    }
}
