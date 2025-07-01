package gift.service;

import gift.dto.api.ProductUpdateRequestDto;
import gift.entity.Product;

import gift.repository.ApprovedProductRepository;
import gift.repository.ProductRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ApprovedProductRepository approvedRepository;

    public ProductService(ProductRepository repository, ApprovedProductRepository approvedRepository) {
        this.repository = repository;
        this.approvedRepository = approvedRepository;
    }

    public Product registerProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(long id) {
        return repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));
    }

    // 기존 객체(existing)는 setter가 없는 불변 객체이므로 값을 수정할 수 없음
    // 따라서 수정된 정보를 반영한 새로운 Product 인스턴스(updated)를 생성하여 업데이트
    public Product updateProduct(long id, ProductUpdateRequestDto updateRequestDto) {
        Product existing = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));

        // 기존 객체와 무관하게 새로운 Product 객체 생성
        Product updated = new Product(id, updateRequestDto.getName(), updateRequestDto.getPrice(), updateRequestDto.getImageUrl());

        repository.update(updated);
        return updated;  // 또는 existing → 필요에 따라 선택 가능
    }

    public void deleteProduct(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new NoSuchElementException("상품을 찾을 수 없습니다.");
        }
        repository.delete(id);
    }

    // "카카오" 문구 검증 로직 공통 메서드
    private void validateKakao(String name) {
        if (name.contains("카카오")) {
            if (!approvedRepository.isApproved(name)) {
                throw new IllegalArgumentException("'카카오'가 포함된 상품은 MD 승인이 필요합니다.");
            }
        }
    }

}
