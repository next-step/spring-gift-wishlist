package gift.repository;

import gift.entity.Product;
import gift.entity.WishProduct;
import gift.exception.ProductNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
            .withTableName("wishes")
            .usingColumns("wish_id", "user_id", "product_name", "quantity");
        this.userJdbcRepository = userJdbcRepository;
        this.productJdbcRepository = productJdbcRepository;
    }

    private RowMapper<WishProduct> wishRowMapper() {
        return (rs, rowNum) -> new WishProduct(
            rs.getString("product_name"),
            rs.getInt("quantity")
        );
    }

    @Override
    public long addProduct(String productName, String email) {
        Optional<Product> product = productJdbcRepository.findByName(productName);
        long userId = userJdbcRepository.findUserIdByEmail(email);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다");
        }

        Map<String, Object> parameters = Map.of(
            "wish_id", userId,
            "user_id", userId,
            "product_name", productName,
            "quantity", 1);

        jdbcInsert.execute(parameters);
        return userId;
    }

    @Override
    public List<WishProduct> getWishList(String email) {
        long userId = userJdbcRepository.findUserIdByEmail(email);

        return jdbcTemplate.query("select * from wishes where user_id = ?",
            wishRowMapper(),
            userId);
    }

    @Override
    public int deleteProduct(Long wishId, String productName) {
        System.out.println(productName);
        return jdbcTemplate.update("delete from wishes where wish_id = ? and product_name = ?",
            wishId, productName);
    }

    @Override
    public int updateWish(Long wishId, String productName, int quantity) {
        return jdbcTemplate.update(
            "update wishes set quantity = ? where wish_id = ? and product_name = ?"
            , quantity, wishId, productName);
    }
}
