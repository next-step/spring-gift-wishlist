package gift.product.service;

import gift.member.domain.Member;
import gift.member.domain.RoleType;
import gift.member.repository.MemberRepository;
import gift.product.domain.Product;
import gift.product.dto.ProductRequestDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductService(ProductRepository productRepository, MemberRepository memberRespository){
        this.productRepository = productRepository;
        this.memberRepository = memberRespository;
    }

    public Product saveProduct(ProductRequestDto requestDto){

        return productRepository.save(requestDto.name(), requestDto.price(), requestDto.imageUrl());
    }

    public List<Product> getProducts(){
        // 전체 조회 (page 등 추후 구현 필요)
        return productRepository.findAll();
    }

    public Product getProduct(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
    }

    public void update(Long id, ProductRequestDto requestDto){
        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
        productRepository.update(id, requestDto.name(), requestDto.price(), requestDto.imageUrl());
    }

    public void delete(Long id){
        // 존재하지 않으면 무시
        productRepository.deleteById(id);
    }

    private void validateProductNameByRole(String productName, Member member) {
        if (productName.contains("카카오") && member.getRole() != RoleType.ADMIN) {
            throw new IllegalArgumentException("상품 이름에 '카카오'는 포함될 수 없습니다. 담당 MD와 협의해 주세요.");
        }
    }
}
