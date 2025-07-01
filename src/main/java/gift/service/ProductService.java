package gift.service;

import gift.dto.request.ProductRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductResponseDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRespository) {
        this.productRepository = productRespository;
    }

    //상품 단 건 조회
    public Product getProduct(long productId) {
        if(!containsProduct(productId)){throw new ProductNotFoundException("상품을 찾을 수 없습니다");}
        return productRepository.findById(productId).orElseThrow();
    }

    //상품 추가
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product(
            productRequestDto.productId(),
            productRequestDto.name(),
            productRequestDto.price(),
            productRequestDto.imageURL());
        productRepository.createProduct(product);

        return product.toResponseDto();
    }

    //상품 수정
    public ProductResponseDto updateProduct (long productId,
        ProductUpdateRequestDto productUpdateRequestDto) {
        if(!containsProduct(productId)){throw new ProductNotFoundException("상품을 찾을 수 없습니다");}
        Product product = new Product(
            productId,
            productUpdateRequestDto.name(),
            productUpdateRequestDto.price(),
            productUpdateRequestDto.imageURL());
        productRepository.updateProduct(product);

        return product.toResponseDto();
    }

    //상품 삭제
    public void deleteProduct(long productId) {
        if(!containsProduct(productId)){throw new ProductNotFoundException("상품을 찾을 수 없습니다");}
        productRepository.delete(productId);
    }

    //상품 유무 확인
    public boolean containsProduct(long productId) {
        return productRepository.containsKey(productId);
    }
}
