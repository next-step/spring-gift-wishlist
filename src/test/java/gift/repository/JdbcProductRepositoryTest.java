// src/test/java/gift/repository/JdbcProductRepositoryTest.java
package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.entity.product.Product;
import gift.exception.custom.ProductNotFoundException;
import gift.repository.product.ProductRepository;
import gift.repository.product.ProductRepositoryImpl;
import gift.repository.product.ProductRowMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@Import({ProductRepositoryImpl.class, ProductRowMapper.class})
class JdbcProductRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUpSchema() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS product");
        jdbcTemplate.execute(
                "CREATE TABLE product (" +
                        "  id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "  name VARCHAR(255) NOT NULL, " +
                        "  price INT NOT NULL, " +
                        "  image_url VARCHAR(1024) NOT NULL, " +
                        "  hidden BOOLEAN NOT NULL DEFAULT FALSE" +
                        ")"
        );
    }


    @Test
    @DisplayName("저장 시 ID 자동 생성 및 findById 조회")
    void saveAndFindById() {
        Product p = Product.of("Test", 100, "http://example.com/test.png");
        Product saved = repository.save(p);

        assertThat(saved.id()).isNotNull();
        Optional<Product> fetched = repository.findById(saved.id().id());
        assertThat(fetched).isPresent()
                .get()
                .extracting(Product::name, Product::price, Product::imageUrl, Product::hidden)
                .containsExactly(p.name(), p.price(), p.imageUrl(), p.hidden());
    }

    @Test
    @DisplayName("findAll은 모든 레코드를 반환")
    void findAllReturnsAll() {
        repository.save(Product.of("A", 1, "http://example.com/a.png"));
        repository.save(Product.of("B", 2, "http://example.com/b.png"));

        List<Product> list = repository.findAll();
        assertThat(list).hasSize(2)
                .extracting(p -> p.name().name())
                .containsExactlyInAnyOrder("A", "B");
    }

    @Test
    @DisplayName("existsById는 올바른 존재 여부를 반환")
    void existsById() {
        Product saved = repository.save(Product.of("X", 5, "http://example.com/x.png"));
        assertThat(repository.existsById(saved.id().id())).isTrue();
        assertThat(repository.existsById(999L)).isFalse();
    }

    @Test
    @DisplayName("update 시 존재하지 않는 ID는 예외 발생")
    void updateNonExistentThrows() {
        Product p = Product.of(999L, "No", 1, "http://example.com/no.png", false);
        assertThatThrownBy(() -> repository.save(p))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("deleteById로 삭제 및 존재하지 않으면 예외")
    void deleteByIdBehavior() {
        Product saved = repository.save(Product.of("Del", 3, "http://example.com/del.png"));
        repository.deleteById(saved.id().id());
        assertThat(repository.existsById(saved.id().id())).isFalse();

        assertThatThrownBy(() -> repository.deleteById(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }
}
