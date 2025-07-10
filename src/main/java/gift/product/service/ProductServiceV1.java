package gift.product.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Role;
import gift.global.exception.BadRequestEntityException;
import gift.global.exception.NotFoundEntityException;
import gift.member.dto.AuthMember;
import gift.member.repository.MemberRepository;
import gift.product.dto.ProductCreateRequest;
import gift.product.dto.ProductResponse;
import gift.product.dto.ProductUpdateRequest;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class ProductServiceV1 implements ProductService{

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductServiceV1(ProductRepository productRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }


    public UUID save(ProductCreateRequest dto, String email) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다"));
        return productRepository.save(new Product(dto.getName(), dto.getPrice(), dto.getImageURL(), findMember.getId()));
    }

    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll()
                .stream().map(ProductResponse::new)
                .toList();
    }


    public ProductResponse findProduct(UUID id) {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("상품이 존재하지 않습니다."));
        return new ProductResponse(findProduct);
    }

    public void deleteProduct(UUID id, AuthMember authMember) {

        Product findProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("상품이 존재하지 않습니다."));

        checkAuthorization(authMember, findProduct.getMemberId());

        productRepository.deleteById(id);
    }

    public void updateProduct(UUID id, ProductUpdateRequest dto, AuthMember authMember) {


        Product findProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("상품이 존재하지 않습니다."));

        UUID memberId = checkAuthorization(authMember, findProduct.getMemberId());

        productRepository.update(new Product(id, dto.getName(), dto.getPrice(), dto.getImageURL(), memberId));
    }

    @Override
    public List<ProductResponse> findByMember(AuthMember authMember) {
        Member findMember = memberRepository.findByEmail(authMember.getEmail())
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다"));

       return productRepository.findByMemberId(findMember.getId())
                .stream().map(ProductResponse::new).toList();
    }

    private UUID checkAuthorization(AuthMember authMember, UUID productMemberId) {

        Member findMember = memberRepository.findByEmail(authMember.getEmail())
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다"));

        if (authMember.getRole() == Role.ADMIN) return findMember.getId();


        if (!findMember.getId().equals(productMemberId))
            throw new BadRequestEntityException("자신의 상품이 아닙니다.");

        return findMember.getId();
    }
}
