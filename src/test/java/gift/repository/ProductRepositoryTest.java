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
class ProductRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository(jdbcClient);

        jdbcClient.sql("DELETE FROM product").update();
        jdbcClient.sql("ALTER TABLE product ALTER COLUMN id RESTART WITH 1").update();

        Product product1 = Product.of(null, "상품1", 10000, "image1.jpg");
        Product product2 = Product.of(null, "상품2", 20000, "image2.jpg");

        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void findAllTest() {
        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(2);
        assertThat(products.get(0).getId()).isEqualTo(1L);
        assertThat(products.get(0).getName()).isEqualTo("상품1");
        assertThat(products.get(1).getId()).isEqualTo(2L);
        assertThat(products.get(1).getName()).isEqualTo("상품2");
    }

    @Test
    void findByIdTest() {
        // when
        var product = productRepository.findById(1L);

        // then
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("상품1");
        assertThat(product.get().getPrice()).isEqualTo(10000);
    }

    @Test
    void findByIdEmptyTest() {
        // when
        var product = productRepository.findById(999L);

        // then
        assertThat(product).isEmpty();
    }

    @Test
    void existsByIdTest() {
        // when & then
        assertThat(productRepository.existsById(1L)).isTrue();
        assertThat(productRepository.existsById(999L)).isFalse();
    }

    @Test
    void saveTest() {
        // given
        Product product = Product.of(null, "새 상품", 30000, "new-image.jpg");

        // when
        Long savedId = productRepository.save(product);

        // then
        assertThat(savedId).isPositive();

        var savedProduct = productRepository.findById(savedId);
        assertThat(savedProduct).isPresent();
        assertThat(savedProduct.get().getName()).isEqualTo("새 상품");
        assertThat(savedProduct.get().getPrice()).isEqualTo(30000);
    }

    @Test
    void updateTest() {
        // given
        Product product = Product.of(1L, "수정된 상품", 15000, "updated-image.jpg");

        // when
        int updatedCount = productRepository.update(product);

        // then
        assertThat(updatedCount).isEqualTo(1);

        var updatedProduct = productRepository.findById(1L);
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getName()).isEqualTo("수정된 상품");
        assertThat(updatedProduct.get().getPrice()).isEqualTo(15000);
        assertThat(updatedProduct.get().getImageUrl()).isEqualTo("updated-image.jpg");
    }

    @Test
    void deleteTest() {
        // when
        int deletedCount = productRepository.delete(1L);

        // then
        assertThat(deletedCount).isEqualTo(1);
        assertThat(productRepository.existsById(1L)).isFalse();
    }
}
