package gift.service;

import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishItem;
import gift.exception.InvalidFieldException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.WishItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishItemRepository wishItemRepository;
    private final ProductRepository productRepository;

    public WishService(WishItemRepository wishItemRepository, ProductRepository productRepository) {
        this.wishItemRepository = wishItemRepository;
        this.productRepository = productRepository;
    }

    public List<WishResponse> getWishlist(Member member) {
        List<WishItem> wishItems = wishItemRepository.findByMember(member);
        return wishItems.stream()
            .map(item -> new WishResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                member.getId()
            ))
            .collect(Collectors.toList());
    }

    public WishResponse addToWishlist(WishRequest request, Member member) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        if (request.productId() == null) {
            throw new InvalidFieldException("Invalid productId");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new InvalidFieldException("Invalid quantity");
        }

        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ProductNotFoundException(
                "Product(id: " + request.productId() + ") not found"));
        WishItem wishItem = new WishItem(
            null,
            product,
            request.quantity(),
            member
        );
        WishItem savedItem = wishItemRepository.save(wishItem);

        return new WishResponse(
            savedItem.getId(),
            savedItem.getProduct().getId(),
            savedItem.getProduct().getName(),
            savedItem.getQuantity(),
            member.getId()
        );
    }

    public void removeFromWishlist(Long productId, Member member) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId cannot be null");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        wishItemRepository.deleteByItemAndMember(productId, member);
    }

}
