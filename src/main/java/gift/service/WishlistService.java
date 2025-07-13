package gift.service;

import gift.model.Product;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Product addProduct(String email, Long productId) {
        boolean exist = wishlistRepository.existsByUserEmailAndProductId(email, productId);
        if (!exist) {
            try {
                Product product = productRepository
                        .findById(productId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다"))
                        .toEntity();
                wishlistRepository.save(email, product);
                return product;
            } catch (NoSuchElementException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다");
            }
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다"))
                .toEntity();

    }

    public void deleteProduct(String email, Long productId) {
        wishlistRepository.deleteByUserEmailAndProductId(email, productId);
    }
}
