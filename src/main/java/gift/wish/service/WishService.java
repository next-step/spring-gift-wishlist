package gift.wish.service;

import gift.product.dto.ProductResponse;
import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<ProductResponse> getWishes(Member member) {
        try {
            List<Product> products = wishRepository.findAllProductsByMemberId(member.getId());
            return products.stream()
                    .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImgUrl()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void addWish(Member member, Long productId) {
        wishRepository.save(member.getId(), productId);
    }

    public void deleteWish(Member member, Long productId) {
        wishRepository.delete(member.getId(), productId);
    }
}