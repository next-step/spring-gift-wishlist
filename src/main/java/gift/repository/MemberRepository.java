package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("MemberRepository")
public class MemberRepository implements MemberRepositoryInterface {
    private JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, this::mapRowToMember, email);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, this::mapRowToMember, email, password);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAllProductsFromWishListByEmail(String email) {
        String sql = "SELECT productId FROM wishlist WHERE email = ?";
        List<Long> wishlistProductId = jdbcTemplate.query(sql, this::mapRowToProductId, email);
        List<Product> wishlistProduct = new ArrayList<>();

        String sql2 = "SELECT * FROM product WHERE id = ?";
        for(Long productId : wishlistProductId) {
            Product product = jdbcTemplate.queryForObject(sql2, this::mapRowToProduct, productId);
            wishlistProduct.add(product);
        }
        return wishlistProduct;
    }

    @Override
    public void addProductToWishListByEmail(String email, Long productId) {
        String sql = "INSERT INTO wishlist (email, productId) VALUES (?, ?)";
        jdbcTemplate.update(sql, email, productId);
    }

    @Override
    public boolean deleteProductFromWishListByEmail(String email, Long productId) {
        String sql = "DELETE FROM wishlist WHERE email = ? AND productId = ?";
        int deleted = jdbcTemplate.update(sql, email, productId);
        return deleted > 0;
    }

    private Member mapRowToMember(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password")
        );
        return member;
    }

    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("imageUrl")
        );
        return product;
    }

    private Long mapRowToProductId(ResultSet rs, int rowNum) throws SQLException {
        Long productId = rs.getLong("productId");
        return productId;
    }
}
