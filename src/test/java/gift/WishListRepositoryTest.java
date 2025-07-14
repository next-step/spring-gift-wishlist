package gift;

import gift.entity.Product;
import gift.repository.WishListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@Import(WishListRepository.class)
class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Test
    void 이메일로_위시리스트_상품_모두_조회() {
        String email = "abc@pusan.ac.kr";

        List<Product> products = wishListRepository.findAllProductsFromWishListByEmail(email);

        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName).containsExactlyInAnyOrder("초코송이", "포스틱");
    }

    @Test
    void 위시리스트에_상품_추가() {
        String email = "def@pusan.ac.kr";
        Long productId = 2L;

        wishListRepository.addProductToWishListByEmail(email, productId);

        List<Product> products = wishListRepository.findAllProductsFromWishListByEmail(email);
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("포스틱");
    }

    @Test
    void 위시리스트에서_상품_삭제() {
        String email = "abcd@pusan.ac.kr";
        Long productId = 1L;

        boolean deleted = wishListRepository.deleteProductFromWishListByEmail(email, productId);

        assertThat(deleted).isTrue();
        assertThat(wishListRepository.findAllProductsFromWishListByEmail(email)).isEmpty();
    }

    @Test
    void 존재하지_않는_상품을_삭제_false_반환() {
        String email = "def@pusan.ac.kr";
        Long productId = 10L;

        boolean deleted = wishListRepository.deleteProductFromWishListByEmail(email, productId);

        assertThat(deleted).isFalse();
    }
}