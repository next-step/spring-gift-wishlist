package gift.service;

import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.WishResponse;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;

    public WishService(ProductRepository productRepository, WishRepository wishRestController, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.wishRepository = wishRestController;
        this.memberRepository = memberRepository;
    }

    public List<Product> productList() {
        return productRepository.findAll();
    }

    public CreateWishResponse addWishProduct(CreateWishRequest request) {
        Wish wish = wishRepository.save(request);
        return new CreateWishResponse(wish.getId(), wish.getMemberId(), wish.getProductId(), wish.getQuantity());
    }

    public List<WishResponse> getMeberWishList(Long memberId) {
        findMemberOrThrow(memberId);
        return wishRepository.findAllByMember(memberId);
    }

    public void delete(Long wishId) {
        findByIdOrThrow(wishId);
        wishRepository.delete(wishId);
    }

    private void findByIdOrThrow(Long wishId) {
        if (wishRepository.findById(wishId).isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 위시리스트 상품입니다.");
        }
    }

    private void findMemberOrThrow(Long memberId) {
        if (memberRepository.findById(memberId).isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 멤버입니다.");
        }
    }
}
