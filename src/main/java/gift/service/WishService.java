package gift.service;

import gift.domain.Product;
import gift.dto.CreateWishRequest;
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

    public void addWishProduct(CreateWishRequest request) {
        wishRepository.save(request);
    }

    public List<WishResponse> getMeberWishList(Long memberId) {
        if (memberRepository.findById(memberId).isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 멤버입니다.");
        }
        return wishRepository.findAllByMember(memberId);
    }
}
