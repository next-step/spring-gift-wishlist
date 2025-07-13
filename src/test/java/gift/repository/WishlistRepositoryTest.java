package gift.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import gift.domain.Product;

@JdbcTest
class WishlistRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    private WishlistRepository wishlistRepository;

    @BeforeEach
    void setUp() {
        wishlistRepository = new WishlistRepository(jdbcClient);

        jdbcClient.sql("DELETE FROM wishlist").update();
        jdbcClient.sql("DELETE FROM product").update();
        jdbcClient.sql("DELETE FROM member").update();

        jdbcClient.sql("ALTER TABLE product ALTER COLUMN id RESTART WITH 1").update();
        jdbcClient.sql("ALTER TABLE member ALTER COLUMN id RESTART WITH 1").update();

        jdbcClient.sql("INSERT INTO member (email, password, role) VALUES ('user1@test.com', 'password1', 'ROLE_USER')").update();
        jdbcClient.sql("INSERT INTO member (email, password, role) VALUES ('user2@test.com', 'password2', 'ROLE_USER')").update();
        jdbcClient.sql("INSERT INTO product (name, price, imageUrl) VALUES ('상품1', 10000, 'image1.jpg')").update();
        jdbcClient.sql("INSERT INTO product (name, price, imageUrl) VALUES ('상품2', 20000, 'image2.jpg')").update();

        jdbcClient.sql("INSERT INTO wishlist (memberId, productId) VALUES (1, 1)").update();
    }

    @Test
    void findAllProductByMemberIdTest() {
        // when
        List<Product> products = wishlistRepository.findAllProductByMemberId(1L);

        // then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getId()).isEqualTo(1L);
        assertThat(products.get(0).getName()).isEqualTo("상품1");
        assertThat(products.get(0).getPrice()).isEqualTo(10000);
    }

    @Test
    void findAllProductByMemberIdEmptyTest() {
        // when
        List<Product> products = wishlistRepository.findAllProductByMemberId(2L);

        // then
        assertThat(products).isEmpty();
    }

    @Test
    void addProductToWishlistTest() {
        // when
        int count = wishlistRepository.addProductToWishlist(2L, 1L);

        // then
        assertThat(count).isEqualTo(1);

        List<Product> products = wishlistRepository.findAllProductByMemberId(2L);
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void deleteProductFromWishlistTest() {
        // when
        int count = wishlistRepository.deleteProductFromWishlist(1L, 1L);

        // then
        assertThat(count).isEqualTo(1);

        List<Product> products = wishlistRepository.findAllProductByMemberId(1L);
        assertThat(products).isEmpty();
    }
}
