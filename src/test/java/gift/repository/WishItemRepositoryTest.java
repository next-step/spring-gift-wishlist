package gift.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishItem;
import gift.exception.InvalidFieldException;
import gift.exception.WishItemNotFoundException;
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
public class WishItemRepositoryTest {

    @Autowired
    private WishItemRepository wishItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Product product;
    private WishItem wishItem;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "test@test.com", "cGFzc3dvcmQxMjM=", "USER");
        product = new Product(1L, "Test Product", 1000, "http://test.com");
        wishItem = new WishItem(1L, product, 2, member);
    }

    @Test
    void save_success() {
        Product newProduct = productRepository.save(
            new Product(null, "New Product", 500, "http://new.com"));
        Member newMember = memberRepository.save(
            new Member(null, "new@test.com", "cGFzc3dvcmQxMjM=", "USER"));
        WishItem newWishItem = new WishItem(null, newProduct, 1, newMember);

        WishItem savedWishItem = wishItemRepository.save(newWishItem);

        assertNotNull(savedWishItem.getId());
        assertEquals(newProduct.getId(), savedWishItem.getProduct().getId());
        assertEquals(1, savedWishItem.getQuantity());
        assertEquals(newMember.getId(), savedWishItem.getMember().getId());
    }

    @Test
    void save_invalidField() {
        WishItem invalidWishItem = new WishItem(null, null, null, null);
        assertThrows(InvalidFieldException.class, () -> wishItemRepository.save(invalidWishItem));
    }

    @Test
    void findByIdAndMember_success() {
        Optional<WishItem> foundWishItem = wishItemRepository.findByIdAndMember(1L, member);
        assertTrue(foundWishItem.isPresent());
        assertEquals(1L, foundWishItem.get().getId());
        assertEquals(2, foundWishItem.get().getQuantity());
    }

    @Test
    void findByIdAndMember_notFound() {
        Member newMember = memberRepository.save(
            new Member(null, "new@test.com", "cGFzc3dvcmQxMjM=", "USER"));
        Optional<WishItem> foundWishItem = wishItemRepository.findByIdAndMember(1L, newMember);
        assertTrue(foundWishItem.isEmpty());
    }

    @Test
    void deleteByIdAndMember_success() {
        assertDoesNotThrow(() -> wishItemRepository.deleteByIdAndMember(1L, member));
        Optional<WishItem> deletedWishItem = wishItemRepository.findByIdAndMember(1L, member);
        assertTrue(deletedWishItem.isEmpty());
    }

    @Test
    void deleteByIdAndMember_notFound() {
        Member newMember = memberRepository.save(
            new Member(null, "new@test.com", "cGFzc3dvcmQxMjM=", "USER"));
        assertThrows(WishItemNotFoundException.class,
            () -> wishItemRepository.deleteByIdAndMember(1L, newMember));
    }

    @Test
    void findByMember_success() {
        List<WishItem> wishItems = wishItemRepository.findByMember(member);
        assertFalse(wishItems.isEmpty());
        assertEquals(1, wishItems.size());
        assertEquals(1L, wishItems.get(0).getId());
        assertEquals(2, wishItems.get(0).getQuantity());
    }

    @Test
    void findByMember_empty() {
        Member newMember = memberRepository.save(
            new Member(null, "new@test.com", "cGFzc3dvcmQxMjM=", "USER"));
        List<WishItem> wishItems = wishItemRepository.findByMember(newMember);
        assertTrue(wishItems.isEmpty());
    }
}