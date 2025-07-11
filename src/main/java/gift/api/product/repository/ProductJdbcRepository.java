package gift.api.product.repository;

import gift.api.product.domain.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private final JdbcClient jdbcClient;

    public ProductJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable, Long categoryId) {
        String where = categoryId != null ? " where category_id = :category_id" : "";
        String order = " order by " + getSortOrder(pageable.getSort());
        String paging = " limit :limit offset :offset";

        var query = jdbcClient.sql("select * from product" + where + order + paging)
                .param("limit", pageable.getPageSize())
                .param("offset", pageable.getOffset());

        var count = jdbcClient.sql("select count(*) from product" + where);

        if (categoryId != null) {
            query = query.param("category_id", categoryId);
            count = count.param("category_id", categoryId);
        }

        return new PageImpl<>(
                query.query(Product.class).list(),
                pageable,
                count.query(Long.class).single()
        );
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return jdbcClient.sql("select * from product where id = :id")
                .param("id", id)
                .query(Product.class)
                .optional();
    }

    @Override
    public Product createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(
                        "insert into product (name, price, image_url) values (:name, :price, :image_url)")
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("image_url", product.getImageUrl())
                .update(keyHolder, "id");

        Long newId = keyHolder.getKey().longValue();

        return findProductById(newId).get();
    }

    @Override
    public Product updateProduct(Product product) {
        jdbcClient.sql(
                        "update product set name = :name, price = :price, image_url = :image_url where id = :id")
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("image_url", product.getImageUrl())
                .param("id", product.getId())
                .update();

        return findProductById(product.getId()).get();
    }

    @Override
    public boolean deleteProduct(Long id) {
        int deleted = jdbcClient.sql("delete from product where id = :id")
                .param("id", id)
                .update();

        return deleted > 0;
    }

    private String getSortOrder(Sort sort) {
        if (!sort.isSorted()) {
            return "id ASC";
        }

        return sort.stream()
                .map(order -> order.getProperty() + (order.isDescending() ? " DESC" : " ASC"))
                .reduce((a, b) -> a + ", " + b)
                .orElse("id ASC");
    }
}