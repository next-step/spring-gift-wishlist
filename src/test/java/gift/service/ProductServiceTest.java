// src/test/java/gift/service/ProductServiceTest.java
package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.entity.product.Product;
import gift.exception.custom.ProductNotFoundException;
import gift.repository.product.ProductRepository;
import gift.service.product.ProductServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void getAllProducts_returnsListFromRepo() {
        Product p = Product.of(1L, "A", 100, "http://example.png", false);
        when(repo.findAll()).thenReturn(List.of(p));

        List<Product> result = service.getAllProducts();

        assertThat(result).containsExactly(p);
        verify(repo).findAll();
    }

    @Test
    void getProductById_existingId_returnsOptionalContainingProduct() {
        Product p = Product.of(1L, "A", 100, "http://example.png", false);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Optional<Product> result = service.getProductById(1L);

        assertThat(result).isPresent().contains(p);
        verify(repo).findById(1L);
    }

    @Test
    void getProductById_missingId_returnsEmptyOptional() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = service.getProductById(99L);

        assertThat(result).isEmpty();
        verify(repo).findById(99L);
    }

    @Test
    void createProduct_allowedName_savesWithHiddenFalse() {
        Product toSave = Product.of("Good", 200, "http://example.png");
        Product saved = toSave.withHidden(false).withId(1L);
        when(repo.save(any())).thenReturn(saved);

        Product result = service.createProduct("Good", 200, "http://example.png");

        assertThat(result.hidden()).isFalse();
        verify(repo).save(argThat(p -> !p.hidden() && p.name().name().equals("Good")));
    }

    @Test
    void createProduct_forbiddenName_savesWithHiddenTrue() {
        Product toSave = Product.of("카카오톡", 300, "http://example.png");
        Product saved = toSave.withHidden(true).withId(2L);
        when(repo.save(any())).thenReturn(saved);

        Product result = service.createProduct("카카오톡", 300, "http://example.png");

        assertThat(result.hidden()).isTrue();
        verify(repo).save(argThat(Product::hidden));
    }

    @Test
    void updateProduct_existing_updatesFields() {
        Product existing = Product.of(1L, "Old", 100, "http://example.png", false);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        Product updated = existing.withName("New").withPrice(150)
                .withImageUrl("http://example.com/new.png");
        when(repo.save(any())).thenReturn(updated);

        Product result = service.updateProduct(1L, "New", 150, "http://example.com/new.png");

        assertThat(result.name().name()).isEqualTo("New");
        assertThat(result.price().price()).isEqualTo(150);
        assertThat(result.imageUrl().url()).isEqualTo("http://example.com/new.png");
        verify(repo).findById(1L);
        verify(repo).save(any());
    }

    @Test
    void updateProduct_missing_throws() {
        when(repo.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProduct(5L, "X", 100, "http://example.com/new.png"))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteProduct_existing_deletes() {
        when(repo.existsById(1L)).thenReturn(true);

        service.deleteProduct(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deleteProduct_missing_throws() {
        when(repo.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProduct(2L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void hideProduct_setsHiddenTrue() {
        Product existing = Product.of(1L, "A", 100, "http://example.com/new.png", false);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        Product hidden = existing.withHidden(true);
        when(repo.save(any())).thenReturn(hidden);

        service.hideProduct(1L);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().hidden()).isTrue();
    }

    @Test
    void unhideProduct_setsHiddenFalse() {
        Product existing = Product.of(1L, "A", 100, "http://example.png", true);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        Product unhidden = existing.withHidden(false);
        when(repo.save(any())).thenReturn(unhidden);

        service.unhideProduct(1L);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().hidden()).isFalse();
    }
}
