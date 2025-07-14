package gift.service;

import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.ProductRepositoryImpl;
import gift.repository.WishRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepositoryImpl productRepositoryImpl;

    public WishService(WishRepository wishRepository, ProductRepositoryImpl productRepositoryImpl) {
        this.wishRepository = wishRepository;
        this.productRepositoryImpl = productRepositoryImpl;
    }

    public void addWish(Long memberId, Long productId) {

        if (wishRepository.exists(memberId, productId)) {
            throw new IllegalArgumentException("이미 위시리스트에 추가된 상품입니다.");
        }

        wishRepository.insert(memberId, productId);
    }

    public void removeWish(Long memberId, Long productId) {
        wishRepository.delete(memberId, productId);
    }

    public List<Product> getAllWish(Long memberId) {

        List<Wish> wishlist = wishRepository.findAllByMemberId(memberId);

        List<Product> products = new ArrayList<>();

        for(Wish wish : wishlist) {
            productRepositoryImpl.findById(wish.getProductId())
                    .ifPresent(products::add);
        }

        return products;
    }
}
