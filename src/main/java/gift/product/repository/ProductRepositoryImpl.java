package gift.product.repository;

import gift.common.pagination.PageRequestDto;
import gift.common.pagination.PageResult;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcClient jdbcClient;
    private static final RowMapper<Product> getProductRowMapper() {
        return (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var price = rs.getInt("price");
            var imageUrl = rs.getString("image_url");
            return new Product(id, name, price, imageUrl);
        };
    }

    public ProductRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Product createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var sql = "INSERT INTO product(name, price, image_url) VALUES (:name, :price, :imageUrl);";
        jdbcClient.sql(sql)
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("imageUrl", product.getImageUrl())
                .update(keyHolder);

        return new Product(keyHolder.getKey().longValue(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Override
    public PageResult<Product> findAllProducts(PageRequestDto pageRequestDto) {
        int page = Math.max(pageRequestDto.page(), 0);
        int size = pageRequestDto.size();
        int offset = page * size;

        String sql = "SELECT id, name, price, image_url FROM product LIMIT :limit OFFSET :offset";

        List<Product> content = jdbcClient.sql(sql)
                .param("limit", size)
                .param("offset", offset)
                .query(getProductRowMapper())
                .list();

        int total = jdbcClient.sql("SELECT COUNT(*) FROM product")
                .query(Integer.class)
                .single();

        int totalPages = (int) Math.ceil((double) total / size);

        return new PageResult<>(content, page, totalPages, size, total);
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        String sql = "SELECT id, name, price, image_url FROM product WHERE id = :id";

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(getProductRowMapper())
                .optional();
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        String sql = "UPDATE product SET name = :name, price = :price, image_url = :imageUrl WHERE id = :id";

        int updated = jdbcClient.sql(sql)
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("imageUrl", product.getImageUrl())
                .param("id", id)
                .update();

        if (updated == 0)
            throw new ProductNotFoundException(id);

        return new Product(id, product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Override
    public void deleteProduct(Long id) {
        String sql = "DELETE FROM product WHERE id = :id";

        int deleted = jdbcClient.sql(sql)
                .param("id", id)
                .update();

        if (deleted == 0)
            throw new ProductNotFoundException(id);
    }
}
