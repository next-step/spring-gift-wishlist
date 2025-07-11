package gift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishItem;
import gift.exception.InvalidFieldException;
import gift.exception.ProductNotFoundException;
import gift.exception.WishItemNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.WishItemRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WishServiceTest {

    @Mock
    private WishItemRepository wishItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    private Member member;
    private Product product;
    private WishItem wishItem;

    @BeforeEach
    void setUp() {
        member = new Member(
            1L,
            "test@test.com",
            "encodedPassword123",
            "USER"
        );
        product = new Product(
            1L,
            "test product",
            1000,
            "test.com"
        );
        wishItem = new WishItem(
            1L,
            product,
            2,
            member
        );
    }

    @Test
    void getWishlistNormalCaseResponse() {
        when(wishItemRepository.findByMember(member)).thenReturn(List.of(wishItem));

        List<WishResponse> result = wishService.getWishlist(member);

        assertNotNull(result);
        assertEquals(1, result.size());
        WishResponse response = result.get(0);
        assertEquals(wishItem.getId(), response.id());
        assertEquals(wishItem.getProduct().getId(), response.productId());
        assertEquals(wishItem.getProduct().getName(), response.name());
        assertEquals(wishItem.getQuantity(), response.quantity());
        assertEquals(member.getId(), response.memberId());
        verify(wishItemRepository, times(1)).findByMember(member);
    }

    @Test
    void getWishListNoItemsEmptyList() {
        when(wishItemRepository.findByMember(member)).thenReturn(Collections.emptyList());

        List<WishResponse> result = wishService.getWishlist(member);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(wishItemRepository, times(1)).findByMember(member);
    }

    @Test
    void addToWishlistNormalCaseResponse() {
        // Given
        WishRequest request = new WishRequest(1L, 1);
        when(productRepository.findById(request.productId())).thenReturn(Optional.of(product));
        when(wishItemRepository.save(any(WishItem.class))).thenAnswer(invocation -> {
            WishItem item = invocation.getArgument(0);
            return new WishItem(2L, product, item.getQuantity(), member);
        });

        WishResponse result = wishService.addToWishlist(request, member);

        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals(product.getId(), result.productId());
        assertEquals(product.getName(), result.name());
        assertEquals(request.quantity(), result.quantity());
        assertEquals(member.getId(), result.memberId());
        verify(productRepository, times(1)).findById(request.productId());
        verify(wishItemRepository, times(1)).save(any(WishItem.class));
    }

    @Test
    void addToWishlistNoProductException() {
        WishRequest request = new WishRequest(1L, 1);
        when(productRepository.findById(request.productId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
            () -> wishService.addToWishlist(request, member));
        verify(productRepository, times(1)).findById(request.productId());
        verify(wishItemRepository, never()).save(any(WishItem.class));
    }

    @Test
    void addToWishlistInvalidQuantityException() {
        WishRequest request = new WishRequest(1L, 0);

        InvalidFieldException exception = assertThrows(InvalidFieldException.class,
            () -> wishService.addToWishlist(request, member));
        assertEquals("Invalid quantity", exception.getMessage());
        verify(productRepository, never()).findById(anyLong());
        verify(wishItemRepository, never()).save(any(WishItem.class));
    }

    @Test
    void removeFromWishlistNormalCase() {
        Long productId = 1L;
        doNothing().when(wishItemRepository).deleteByItemAndMember(productId, member);

        wishService.removeFromWishlist(productId, member);

        verify(wishItemRepository, times(1)).deleteByItemAndMember(productId, member);
    }

    @Test
    void removeFromWishlistNoProductException() {
        Long productId = 1L;
        doThrow(new WishItemNotFoundException("WishItem not found")).when(wishItemRepository)
            .deleteByItemAndMember(productId, member);

        assertThrows(WishItemNotFoundException.class,
            () -> wishService.removeFromWishlist(productId, member));
        verify(wishItemRepository, times(1)).deleteByItemAndMember(productId, member);
    }

}
