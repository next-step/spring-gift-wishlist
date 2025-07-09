package gift.service;

import gift.common.exception.InvalidUserException;
import gift.common.exception.ProductNotFoundException;
import gift.common.exception.WishlistAlreadyExistsException;
import gift.common.exception.WishlistNotFoundException;
import gift.domain.Product;
import gift.domain.Wishlist;
import gift.dto.wishlist.CreateWishlistRequest;
import gift.dto.wishlist.WishlistResponse;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public Wishlist saveWishlist(Long userId, CreateWishlistRequest request) {
        Optional<Product> product = productRepository.findById(request.productId());
        if (product.isEmpty()) {
            throw new ProductNotFoundException(request.productId());
        }
        Optional<Wishlist> byProductId = wishlistRepository.findByProductId(request.productId());
        if (byProductId.isPresent()) {
            throw new WishlistAlreadyExistsException();
        }
        Wishlist wishlist = new Wishlist(userId, request.productId());
        return wishlistRepository.save(wishlist);
    }

    public List<WishlistResponse> getWishlistsByUserId(Long id) {
        return wishlistRepository.findAllByUserId(id);
    }

    public void deleteWishlist(Long userId, Long wishlistId) {
        Wishlist wishlist = getById(wishlistId);
        if (!wishlist.getUserId().equals(userId)) {
            throw new InvalidUserException("해당 요청에 대한 권한이 없습니다.");
        }
        wishlistRepository.deleteById(wishlistId);
    }

    private Wishlist getById(Long id) {
        return wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException(id));
    }
}
