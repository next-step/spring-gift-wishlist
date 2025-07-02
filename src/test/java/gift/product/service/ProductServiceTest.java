package gift.product.service;

import gift.domain.Product;
import gift.global.exception.NotFoundProductException;
import gift.product.dto.ProductCreateRequest;
import gift.product.dto.ProductResponse;
import gift.product.dto.ProductUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("상품 추가, 조회 성공")
    void addProductSuccess() {
        Product product = addProductCase();

        ProductResponse findProduct = productService.findProduct(product.getId());
        assertThat(product.getId()).isEqualTo(findProduct.getId());
        assertThat(product.getName()).isEqualTo(findProduct.getName());
        assertThat(product.getPrice()).isEqualTo(findProduct.getPrice());
        assertThat(product.getImageURL()).isEqualTo(findProduct.getImageURL());
    }

    @Test
    @DisplayName("모든 상품 조회")
    void getAllProductsSuccess() {
        for (int i=0; i<10; i++) {
            addProductCase();
        }

        List<ProductResponse> allProducts = productService.findAllProducts();

        assertThat(allProducts.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품 조회 실패")
    void getProductSuccess() {
        assertThatThrownBy(()-> productService.findProduct(UUID.randomUUID()))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProductSuccess() {
        Product product = addProductCase();
        productService.deleteProduct(product.getId());

        assertThatThrownBy(()-> productService.findProduct(product.getId()))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("상품 삭제 실패")
    void deleteProductFail() {
        assertThatThrownBy(()-> productService.findProduct(UUID.randomUUID()))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("상품 수정 성공")
    void updateProductSuccess() {
        Product product = addProductCase();
        ProductUpdateRequest updateDto = new ProductUpdateRequest("스윙칩", 3500, "data://image");

        productService.updateProduct(product.getId(), updateDto);

        ProductResponse response = productService.findProduct(product.getId());

        assertThat(response.getName()).isEqualTo(updateDto.getName());
        assertThat(response.getPrice()).isEqualTo(updateDto.getPrice());
        assertThat(response.getImageURL()).isEqualTo(updateDto.getImageURL());
    }

    @Test
    @DisplayName("상품 수정 실패")
    void updateProductFail() {
        ProductUpdateRequest updateDto = new ProductUpdateRequest("스윙칩", 3500, "data://image");

        assertThatThrownBy(()->productService.updateProduct(UUID.randomUUID(), updateDto))
                .isInstanceOf(NotFoundProductException.class);

    }

    private Product addProductCase() {
        UUID uuid = productService.addProduct(new ProductCreateRequest("스윙칩", 3000, "data://image"));
        return new Product(uuid, "스윙칩", 3000, "data://image");
    }
}