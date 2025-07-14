package gift.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import gift.domain.Product;
import gift.dto.CreateProductRequest;
import gift.dto.CreateProductResponse;
import gift.dto.ProductResponse;
import gift.exception.ApprovalRequiredException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;

class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);

    private final ProductService productService = new ProductService(productRepository);

    @Test
    void getAllProductsTest() {
        // given
        given(productRepository.findAll()).willReturn(List.of(
            Product.of(1L, "상품1", 1000L, "image1"),
            Product.of(2L, "상품2", 2000L, "image2")
        ));

        // when
        List<ProductResponse> response = productService.getAllProducts();

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).id()).isEqualTo(1L);
        assertThat(response.get(0).name()).isEqualTo("상품1");
        assertThat(response.get(0).price()).isEqualTo(1000L);
        assertThat(response.get(0).imageUrl()).isEqualTo("image1");
        assertThat(response.get(1).id()).isEqualTo(2L);
        assertThat(response.get(1).name()).isEqualTo("상품2");
        assertThat(response.get(1).price()).isEqualTo(2000L);
        assertThat(response.get(1).imageUrl()).isEqualTo("image2");
    }

    @Test
    void getProductByIdTest() {
        // given
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.of(
            Product.of(productId, "상품1", 1000L, "image")
        ));

        // when
        ProductResponse response = productService.getProductById(productId);

        // then
        assertThat(response.id()).isEqualTo(productId);
        assertThat(response.name()).isEqualTo("상품1");
        assertThat(response.price()).isEqualTo(1000L);
        assertThat(response.imageUrl()).isEqualTo("image");
    }

    @Test
    void getProductByIdFailTest() {
        // given
        Long failId = 9999L;
        given(productRepository.findById(failId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> productService.getProductById(failId))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void createProductTest() {
        // given
        CreateProductRequest request = new CreateProductRequest("상품1", 1000L, "image");
        Product saved = Product.of(1L, "상품1", 1000L, "image");
        given(productRepository.save(any())).willReturn(1L);
        given(productRepository.findById(1L)).willReturn(Optional.of(saved));

        // when
        CreateProductResponse response = productService.createProduct(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("상품1");
        assertThat(response.price()).isEqualTo(1000L);
        assertThat(response.imageUrl()).isEqualTo("image");
    }

    @Test
    void createProductFailTest() {
        // given
        CreateProductRequest request = new CreateProductRequest("카카오닙스", 1000L, "image");

        // when, then
        assertThatThrownBy(() -> productService.createProduct(request))
            .isInstanceOf(ApprovalRequiredException.class);
    }
}
