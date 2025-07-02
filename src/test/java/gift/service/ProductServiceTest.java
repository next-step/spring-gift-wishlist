package gift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("카카오 포함")
    void saveProduct_withKakaoInName_shouldThrowException() {
        ProductRequestDto requestDto = new ProductRequestDto("카카오", 12000,
                "http://img.com/img.jpg");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.saveProduct(requestDto);
        });

        assertEquals("\"카카오\"가 포함된 상품명 사용 불가", exception.getReason());
    }

    @Test
    @DisplayName("정상 상품 등록")
    void saveProduct_success() {
        ProductRequestDto requestDto = new ProductRequestDto("딸기케이크", 15000,
                "http://img.com/img.jpg");
        Product savedProduct = new Product(1L, "딸기케이크", 15000, "http://img.com/img.jpg");

        when(productRepository.saveProduct(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDto result = productService.saveProduct(requestDto);

        assertEquals("딸기케이크", result.name());
        assertEquals(15000, result.price());
        assertEquals("http://img.com/img.jpg", result.imageUrl());
    }
}
