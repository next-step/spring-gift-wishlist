package gift.service;

import gift.model.Product;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public List<Product> getWishlist(String email) {
        return wishlistRepository.findByUserEmail(email);
    }

    public void addProduct(String email, Long productId) {
        boolean exist = wishlistRepository.existsByUserEmailAndProductId(email, productId);
        if(!exist) {
            Product product = productRepository
                    .findById(productId)
                    .orElseThrow(()->new NoSuchElementException("상품을 찾을 수 없습니다: "))
                    .toEntity();
            wishlistRepository.save(email, product);
        }
    }

    public void deleteProduct(String email, Long productId) {
        wishlistRepository.deleteByUserEmailAndProductId(email, productId);
    }
}
