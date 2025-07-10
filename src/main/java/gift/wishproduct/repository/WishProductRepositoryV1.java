package gift.wishproduct.repository;

import gift.domain.Product;
import gift.domain.WishProduct;
import gift.global.exception.CustomDatabaseException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static gift.util.UUIDParser.bytesToUUID;
import static gift.util.UUIDParser.uuidToBytes;

@Repository
public class WishProductRepositoryV1 implements WishProductRepository {

    private final JdbcClient jdbcClient;

    public WishProductRepositoryV1(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public WishProduct save(WishProduct wishProduct) {
        String sql = "insert into wish_product " +
                "values(:id, :product_name, :price, :quantity, :image_url, :member_id, :product_id)";

        int update = jdbcClient.sql(sql)
                .param("id", wishProduct.getId())
                .param("product_name", wishProduct.getProductName())
                .param("price", wishProduct.getPrice())
                .param("image_url", wishProduct.getImageURL())
                .param("quantity", wishProduct.getQuantity())
                .param("member_id", uuidToBytes(wishProduct.getMemberId()))
                .param("product_id", uuidToBytes(wishProduct.getProductId()))
                .update();

        if (update == 0) throw new CustomDatabaseException("위시 상품 저장 실패");

        return wishProduct;
    }

    @Override
    public Optional<WishProduct> findById(UUID id) {

        String sql = "select * from wish_product where id = :id";

        return jdbcClient.sql(sql)
                .param("id", uuidToBytes(id))
                .query(getWishProductRowMapper())
                .optional();
    }

    @Override
    public List<WishProduct> findByMemberId(UUID memberId) {

        String sql = "select * from wish_product where member_id = :member_id";

        return jdbcClient.sql(sql)
                .param("member_id", uuidToBytes(memberId))
                .query(getWishProductRowMapper())
                .list();
    }

    @Override
    public List<WishProduct> findByProductId(UUID productId) {
        String sql = "select * from wish_product where product_id = :product_id";

        return jdbcClient.sql(sql)
                .param("product_id", uuidToBytes(productId))
                .query(getWishProductRowMapper())
                .list();
    }

    @Override
    public Optional<WishProduct> findByMemberIdAndProductId(UUID memberId, UUID productId) {
        String sql = "select * from wish_product where member_id = :member_id and product_id = :product_id";

        return jdbcClient.sql(sql)
                .param("member_id", memberId)
                .param("product_id", productId)
                .query(getWishProductRowMapper())
                .optional();
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "delete from wish_product where id = :id";

        int update = jdbcClient.sql(sql)
                .param("id", uuidToBytes(id))
                .update();

        if (update == 0) throw new CustomDatabaseException("위시 상품 제거 실패");
    }

    @Override
    public void update(WishProduct wishProduct) {
        String sql = "update wish_product set quantity = :quantity where id = :id";

        int update = jdbcClient.sql(sql)
                .param("quantity", wishProduct.getQuantity())
                .param("id", uuidToBytes(wishProduct.getId()))
                .update();

        if (update == 0) throw new CustomDatabaseException("위시 리스트 수정 실패");
    }

    @Override
    public void deleteAll() {
        String sql = "delete from wish_product";

        jdbcClient.sql(sql)
                .update();
    }

    private RowMapper<WishProduct> getWishProductRowMapper() {
        return (rs, rowNum) -> {
            UUID id = bytesToUUID(rs.getBytes("id"));
            String productName = rs.getString("product_name");
            int price = rs.getInt("price");
            int quantity = rs.getInt("quantity");
            String imageUrl = rs.getString("image_url");
            UUID memberId = bytesToUUID(rs.getBytes("member_id"));
            UUID productId = bytesToUUID(rs.getBytes("product_id"));
            return new WishProduct(id, productName, price, quantity, imageUrl, memberId, productId);
        };
    }
}
