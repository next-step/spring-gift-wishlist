package gift.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", 1000, "http://test.com");
    }

    @Test
    void save_success() {
        Product newProduct = new Product(null, "New Product", 500, "http://new.com");
        Product savedProduct = productRepository.save(newProduct);

        assertNotNull(savedProduct.getId());
        assertTrue(savedProduct.getId() > 1);
        assertEquals("New Product", savedProduct.getName());
        assertEquals(500, savedProduct.getPrice());
        assertEquals("http://new.com", savedProduct.getImageUrl());
    }

    @Test
    void save_invalidField() {
        Product invalidProduct = new Product(null, null, null, "http://test.com");
        assertThrows(IllegalArgumentException.class, () -> productRepository.save(invalidProduct));
    }

    @Test
    void update_success() {
        Product updatedProduct = new Product(1L, "Updated Product", 1500, "http://updated.com");
        Product result = productRepository.update(updatedProduct);

        assertEquals("Updated Product", result.getName());
        assertEquals(1500, result.getPrice());
        assertEquals("http://updated.com", result.getImageUrl());

        Optional<Product> verifiedProduct = productRepository.findById(1L);
        assertTrue(verifiedProduct.isPresent());
        assertEquals("Updated Product", verifiedProduct.get().getName());
    }

    @Test
    void update_notFound() {
        Product invalidProduct = new Product(999L, "Nonexistent", 100, "http://test.com");
        assertThrows(IllegalArgumentException.class,
            () -> productRepository.update(invalidProduct));
    }

    @Test
    void findById_success() {
        Optional<Product> foundProduct = productRepository.findById(1L);
        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
    }

    @Test
    void findById_notFound() {
        Optional<Product> foundProduct = productRepository.findById(999L);
        assertTrue(foundProduct.isEmpty());
    }

    @Test
    void delete_success() {
        assertDoesNotThrow(() -> productRepository.delete(123L));
        Optional<Product> deletedProduct = productRepository.findById(123L);
        assertTrue(deletedProduct.isEmpty());
    }

    @Test
    void delete_notFound() {
        assertThrows(IllegalArgumentException.class, () -> productRepository.delete(999L));
    }

    @Test
    void findAll_success() {
        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty());
        assertEquals(2, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }
}