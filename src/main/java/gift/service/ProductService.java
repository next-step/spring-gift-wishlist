package gift.service;

import gift.dto.request.ProductRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductResponseDto;
import gift.entity.Product;
import gift.exception.KakaoApproveException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRespository) {
        this.productRepository = productRespository;
    }

    public ProductResponseDto productToResponseDto(Product product){
        return new ProductResponseDto(product.productId(), product.name(), product.price(), product.imageURL());
    }

    //상품 단 건 조회
    public Product getProduct(long productId) {
        if(!containsProduct(productId)){throw new ProductNotFoundException("상품을 찾을 수 없습니다");}
        return productRepository.findById(productId).orElseThrow();
    }

    //상품 추가
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        if(productRequestDto.name().contains("카카오")){
            throw new KakaoApproveException(
                "\"카카오\" 문구가 들어간 상품은 담당MD와 협의 후 사용할 수 있습니다");}
        Product product = new Product(
            productRequestDto.productId(),
            productRequestDto.name(),
            productRequestDto.price(),
            productRequestDto.imageURL());
        productRepository.createProduct(product);

        return productToResponseDto(product);
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


        return productToResponseDto(product);
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
