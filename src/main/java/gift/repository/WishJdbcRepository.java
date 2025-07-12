package gift.repository;

import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class WishJdbcRepository implements WishRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final UserJdbcRepository userJdbcRepository;
    private final ProductJdbcRepository productJdbcRepository;

    public WishJdbcRepository(JdbcTemplate jdbcTemplate, UserJdbcRepository userJdbcRepository,
        ProductJdbcRepository productJdbcRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
            .withTableName("wish_list")
            .usingColumns("wish_id", "user_id", "product_id", "quantity");
        this.userJdbcRepository = userJdbcRepository;
        this.productJdbcRepository = productJdbcRepository;
    }

    @Override
    public long addProduct(String productName, String email) {
        Optional<Product> product = productJdbcRepository.findByName(productName);
        long userId = userJdbcRepository.findUserIdByEmail(email);
        int quantity = getCurrnetQuantity(productName, userId).get();
        if (product.isEmpty()) {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다");
        }

        Map<String, Object> parameters = Map.of(
            "wish_id", userId,
            "user_id", userId,
            "product_id", product.get().productId(),
            "quantity", quantity + 1);

        jdbcInsert.execute(parameters);
        return userId;
    }

    @Override
    public Optional<Integer> getCurrnetQuantity(String productName, Long userId) {
        long productId = productJdbcRepository
            .findByName(productName)
            .get()
            .productId();
        try {
            Integer quantity = jdbcTemplate.queryForObject(
                "select quantity from wish_list where product_id = ? and user_id = ?",
                Integer.class,
                productId, userId
            );
            return Optional.ofNullable(quantity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
