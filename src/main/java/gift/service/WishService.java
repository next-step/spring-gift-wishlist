package gift.service;

import gift.dto.ProductResponse;
import gift.entity.Member;
import gift.repository.WishRepository;
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
        return wishRepository.findProductsByMemberId(member.getId()).stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()))
                .collect(Collectors.toList());
    }

    public void addWish(Member member, Long productId) {
        wishRepository.save(member.getId(), productId);
    }

    public void deleteWish(Member member, Long productId) {
        wishRepository.delete(member.getId(), productId);
    }
}