package gift.product.service;

import gift.domain.Product;
import gift.global.exception.NotFoundEntityException;
import gift.product.dto.ProductCreateRequest;
import gift.product.dto.ProductResponse;
import gift.product.dto.ProductUpdateRequest;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceV1 implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceV1(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public UUID save(ProductCreateRequest dto) {

        return productRepository.save(new Product(dto.getName(), dto.getPrice(), dto.getImageURL()));
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

    public void deleteProduct(UUID id) {

        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("상품이 존재하지 않습니다."));

        productRepository.deleteById(id);
    }

    public void updateProduct(UUID id, ProductUpdateRequest dto) {
        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("상품이 존재하지 않습니다."));

        productRepository.update(new Product(id, dto.getName(), dto.getPrice(), dto.getImageURL()));
    }
}
