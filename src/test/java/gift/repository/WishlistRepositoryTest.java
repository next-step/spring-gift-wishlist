package gift.repository;

import gift.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Test
    void findByUserEmail_찾은_상품이_있는_경우() {
        // given
        String userEmail = "abcd@gmail.com";
        Product product = new Product(1L, "상품1", 1000, "https://media.istockphoto.com/id/1667499762/ko/%EB%B2%A1%ED%84%B0/%EC%98%81%EC%97%85%EC%A4%91-%ED%8C%90%EC%A7%80-%EC%83%81%EC%9E%90.jpg?s=612x612&w=0&k=20&c=94uRFQLclgFtnDhE4OfO1tCJdETL3uuBM9ZHD_N4P4Y=");
        wishlistRepository.save(userEmail, product);

        // when
        List<Product> products = wishlistRepository.findByUserEmail(userEmail);

        // then
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("상품1");
        assertThat(products.get(0).getPrice()).isEqualTo(1000);
    }

    @Test
    void findByUserEmail_찾은_상품이_없는_경우() {
        // given
        String userEmail = "nonexistent@gmail.com";

        // when
        List<Product> products = wishlistRepository.findByUserEmail(userEmail);

        // then
        assertThat(products).isEmpty();
    }

    @Test
    void existsByUserEmailAndProductId_존재하는_경우() {
        // given
        String userEmail = "abcd@gmail.com";
        Product product = new Product(1L, "상품1", 1000, "https://example.com/image.jpg");
        wishlistRepository.save(userEmail, product);

        // when
        boolean exists = wishlistRepository.existsByUserEmailAndProductId(userEmail, 1L);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByUserEmailAndProductId_존재하지_않는_경우() {
        // given
        String userEmail = "abcd@gmail.com";
        Long nonExistentProductId = 999L;

        // when
        boolean exists = wishlistRepository.existsByUserEmailAndProductId(userEmail, nonExistentProductId);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void deleteByUserEmailAndProductId_삭제_성공() {
        // given
        String userEmail = "abcd@gmail.com";
        Product product = new Product(1L, "상품1", 1000, "https://example.com/image.jpg");
        wishlistRepository.save(userEmail, product);

        // when
        wishlistRepository.deleteByUserEmailAndProductId(userEmail, 1L);

        // then
        boolean exists = wishlistRepository.existsByUserEmailAndProductId(userEmail, 1L);
        assertThat(exists).isFalse();
    }
}