package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.*;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public UpdateWishResponse updateQuantity(UpdateWishRequest request, Long wishId) {
        Wish wishProduct = findByIdOrThrow(wishId);
        wishRepository.update(request, wishId);
        return new UpdateWishResponse(wishProduct.getId(), wishProduct.getProductId(), request.quantity());
    }

    private Wish findByIdOrThrow(Long wishId) {
        Optional<Wish> findWishProduct = wishRepository.findById(wishId);
        if (findWishProduct.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 위시리스트 상품입니다.");
        }
        return findWishProduct.get();
    }

    private Member findMemberOrThrow(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 멤버입니다.");
        }
        return findMember.get();
    }
}
