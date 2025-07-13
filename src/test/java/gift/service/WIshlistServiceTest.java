package gift.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import gift.domain.Product;
import gift.dto.AddWishlistRequest;
import gift.dto.ProductResponse;
import gift.exception.ProductNotFoundException;
import gift.exception.WishlistDeleteException;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;

public class WIshlistServiceTest {

    private final WishlistRepository wishlistRepository = mock(WishlistRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);

    private final WishlistService wishlistService = new WishlistService(
        wishlistRepository,
        productRepository
    );

    @Test
    void getProductsFromWishlistTest() {
        //given
        Long memberId = 1L;
        given(wishlistRepository.findAllProductByMemberId(memberId)).willReturn(List.of(
            Product.of(1L, "상품1", 1000, "image1"),
            Product.of(2L, "상품2", 2000, "image2")
        ));

        // when
        List<ProductResponse> response = wishlistService.getProductsFromWishlist(memberId);

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).id()).isEqualTo(1L);
        assertThat(response.get(0).name()).isEqualTo("상품1");
        assertThat(response.get(0).price()).isEqualTo(1000);
        assertThat(response.get(0).imageUrl()).isEqualTo("image1");
        assertThat(response.get(1).id()).isEqualTo(2L);
        assertThat(response.get(1).name()).isEqualTo("상품2");
        assertThat(response.get(1).price()).isEqualTo(2000);
        assertThat(response.get(1).imageUrl()).isEqualTo("image2");
    }

    @Test
    void addProductToWishlistTest() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        AddWishlistRequest request = new AddWishlistRequest(productId);
        given(productRepository.existsById(productId)).willReturn(true);
        given(productRepository.findById(productId)).willReturn(Optional.of(
            Product.of(productId, "상품1", 1000, "image1")
        ));
        given(wishlistRepository.addProductToWishlist(memberId, productId)).willReturn(1);

        // when
        ProductResponse response = wishlistService.addProductToWishlist(memberId, request);

        // then
        assertThat(response.id()).isEqualTo(productId);
        assertThat(response.name()).isEqualTo("상품1");
        assertThat(response.price()).isEqualTo(1000);
        assertThat(response.imageUrl()).isEqualTo("image1");
    }

    @Test
    void addProductToWishlistFailTest() {
        // given
        Long memberId = 1L;
        Long failProductId = 999L;
        AddWishlistRequest request = new AddWishlistRequest(failProductId);
        given(productRepository.existsById(failProductId)).willReturn(false);
        given(productRepository.findById(failProductId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> wishlistService.addProductToWishlist(memberId, request))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteProductFromWishlistTest() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        given(productRepository.existsById(productId)).willReturn(true);
        given(wishlistRepository.deleteProductFromWishlist(memberId, productId)).willReturn(1);

        // when, then
        assertThatCode(() -> wishlistService.deleteProductFromWishlist(memberId, productId))
            .doesNotThrowAnyException();
    }

    @Test
    void deleteProductFromWishlistFailTest() {
        // given
        Long memberId = 1L;
        Long failProductId = 999L;
        given(productRepository.existsById(failProductId)).willReturn(true);
        given(wishlistRepository.deleteProductFromWishlist(memberId, failProductId)).willReturn(0);

        // when, then
        assertThatThrownBy(() -> wishlistService.deleteProductFromWishlist(memberId, failProductId))
            .isInstanceOf(WishlistDeleteException.class);
    }
}
