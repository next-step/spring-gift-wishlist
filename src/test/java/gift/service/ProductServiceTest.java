package gift.service;

import gift.repository.ProductRepository;
import gift.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품이_정상적으로_등록된다() {
        // given
        String name = "초코파이";
        int price = 1000;
        String imageUrl = "http://example.com/chocopie.jpg";

        // when
        Product result = productService.create(name, price, imageUrl);

        // then
        assertNotNull(result);
        assertEquals("초코파이", result.getName());
    }

    @Test
    void 상품명에_카카오가_포함되면_예외가_발생한다() {
        // given
        String name = "카카오";
        int price = 2000;
        String imageUrl = "http://example.com/kakao.jpg";

        // when & then
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(name, price, imageUrl)
        );

        assertEquals("'카카오'가 포함된 상품명은 MD와 협의 후 등록 가능합니다.", e.getMessage());
    }
}
