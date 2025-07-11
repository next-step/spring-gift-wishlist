package gift.wishproduct.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishProduct;
import gift.global.exception.BadRequestEntityException;
import gift.global.exception.NotFoundEntityException;
import gift.member.repository.MemberRepository;
import gift.product.repository.ProductRepository;
import gift.wishproduct.dto.WishProductCreateReq;
import gift.wishproduct.dto.WishProductResponse;
import gift.wishproduct.repository.WishProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishProductServiceV1 implements WishProductService {

    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishProductServiceV1(WishProductRepository wishProductRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishProductRepository = wishProductRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public UUID save(WishProductCreateReq dto, String email) {

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 상품입니다."));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 회원입니다."));

        WishProduct wishProduct = wishProductRepository.findByMemberIdAndProductId(member.getId(), product.getId())
                .orElse(null);

        if (wishProduct == null) {
            WishProduct saved = wishProductRepository.save(new WishProduct(product.getName(), product.getPrice(), dto.getQuantity(),
                    product.getImageURL(), member.getId(), product.getId()));

            return saved.getId();
        }

        wishProductRepository.update(new WishProduct(wishProduct.getId(), product.getName(), product.getPrice(), wishProduct.getQuantity()+dto.getQuantity(),
                product.getImageURL(), member.getId(), product.getId()));

        return wishProduct.getId();
    }

    @Override
    public List<WishProductResponse> findMyWishProduct(String email) {

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 회원입니다."));

        return wishProductRepository.findByMemberId(findMember.getId())
                .stream().map(WishProductResponse::new)
                .toList();
    }

    @Override
    public void deleteById(UUID id, String email) {

        WishProduct wishProduct = wishProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 상품입니다."));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 회원입니다."));

        if (!member.getId().equals(wishProduct.getMemberId()))
            throw new BadRequestEntityException("자신의 위시 상품만 삭제할 수 있습니다");

        wishProductRepository.deleteById(wishProduct.getId());
    }


}
