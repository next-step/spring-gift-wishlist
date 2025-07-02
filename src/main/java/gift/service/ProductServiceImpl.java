package gift.service;

import gift.dto.ProductAddRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateRequestDto;
import gift.entity.Product;
import gift.exception.InvalidProductException;
import gift.exception.OperationFailedException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.ProductRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(ProductAddRequestDto requestDto) {
        validateProductName(requestDto.name());
        Product product = new Product(null, requestDto.name(), requestDto.price(), requestDto.url());
        int result = productRepository.addProduct(product);
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    @Override
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findProductByIdOrElseThrow(id);
        return new ProductResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> findAllProduct() {
        List<Product> products = productRepository.findAllProduct();
        List<ProductResponseDto> responseDtos = products.stream().map(Product::toProductResponseDto).toList();
        return responseDtos;
    }

    @Override
    public void updateProductById(Long id, ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findProductByIdOrElseThrow(id);
        validateProductName(requestDto.name());
        Product newProduct = new Product(id, requestDto);
        int result = productRepository.updateProductById(newProduct);
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findProductByIdOrElseThrow(id);
        int result = productRepository.deleteProductById(product.id());
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    @Override
    public void validateProductName(String name) {
        if (!name.matches("^[a-zA-Z0-9ㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]*$")) {
            throw new InvalidProductException("상품명에 허용되지 않는 특수 문자가 포함되어 있습니다.");
        }

        if (name.contains("카카오")) {
            throw new InvalidProductException("\"카카오\"가 포함된 상품명은 MD 협의 후 사용할 수 있습니다.");
        }
    }
}
