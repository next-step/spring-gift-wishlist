package gift.service;

import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishServiceTest {

    private WishRepository wishRepository;
    private ProductRepository productRepository;
    private WishServiceImpl wishService;

    @BeforeEach
    void setUp() {
        wishRepository = mock(WishRepository.class);
        productRepository = mock(ProductRepository.class);
        wishService = new WishServiceImpl(wishRepository, productRepository);
    }

    @Test
    @DisplayName("상품이 존재하면, 위시를 추가할 수 있다. ")
    void shouldCreateWish() {
        Long memberId = 1L;
        Long productId = 2L;

        when(wishRepository.existsWishByMemberIdAndProductId(memberId, productId)).thenReturn(false);
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(new Product(productId, "하리보 젤리", 1500, "http://img.url/test.png")));
        when(wishRepository.createWish(any(Wish.class))).thenReturn(new Wish(10L, memberId, productId));

        WishResponseDto result = wishService.createWish(memberId, productId);

        assertThat(result.productId()).isEqualTo(productId);
        assertThat(result.productName()).isEqualTo("하리보 젤리");
    }

    @Test
    @DisplayName("상품이 존재하지 않으면, 404(Not Found) 예외가 발생한다. ")
    void shouldThrowIfProductNotFound() {
        when(productRepository.findProductById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wishService.createWish(1L, 999L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("위시 ID로 삭제 시, deleteWishById가 수행된다. ")
    void shouldDeleteWish() {
        when(wishRepository.existsWishById(5L)).thenReturn(true);
        wishService.deleteWishById(5L);
        verify(wishRepository).deleteWishById(5L);
    }

    @Test
    @DisplayName("회원 ID로 모든 위시를 조회 시, 위시 목록을 반환한다. ")
    void shouldReturnAllWishesByMember() {
        Long memberId = 1L;
        Wish wish = new Wish(1L, memberId, 2L);
        Product product = new Product(2L, "하리보 젤리(콜라맛)", 2000, "http://img.url/coke.png");

        when(wishRepository.findAllWishByMemberId(memberId)).thenReturn(List.of(wish));
        when(productRepository.findProductById(2L)).thenReturn(Optional.of(product));

        List<WishResponseDto> result = wishService.findAllWishesByMemberId(memberId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).productName()).isEqualTo("하리보 젤리(콜라맛)");
    }
}
