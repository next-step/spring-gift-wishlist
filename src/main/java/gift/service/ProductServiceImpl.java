package gift.service;

import gift.dto.request.ProductRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductResponseDto;
import gift.entity.Product;
import gift.exception.KakaoApproveException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRespository) {
        this.productRepository = productRespository;
    }

    private static final String KAKAO_KEYWORD = "카카오";

    public ProductResponseDto productToResponseDto(Product product) {
        return new ProductResponseDto(
            product.productId(),
            product.name(),
            product.price(),
            product.imageURL());
    }


    public Product getProduct(long productId) {
        if (!containsProduct(productId)) {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다");
        }
        return productRepository.findById(productId).orElseThrow();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        if (productRequestDto.name().contains(KAKAO_KEYWORD)) {
            throw new KakaoApproveException(
                "\"카카오\" 문구가 들어간 상품은 담당MD와 협의 후 사용할 수 있습니다");
        }
        Product product = new Product(
            productRequestDto.productId(),
            productRequestDto.name(),
            productRequestDto.price(),
            productRequestDto.imageURL());
        productRepository.createProduct(product);

        return productToResponseDto(product);
    }


    public ProductResponseDto updateProduct(long productId,
        ProductUpdateRequestDto productUpdateRequestDto) {
        if (!containsProduct(productId)) {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다");
        }
        Product product = new Product(
            productId,
            productUpdateRequestDto.name(),
            productUpdateRequestDto.price(),
            productUpdateRequestDto.imageURL());
        productRepository.updateProduct(product);

        return productToResponseDto(product);
    }


    public void deleteProduct(long productId) {
        if (!containsProduct(productId)) {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다");
        }
        productRepository.delete(productId);
    }


    public boolean containsProduct(long productId) {
        return productRepository.productExists(productId);
    }
}
