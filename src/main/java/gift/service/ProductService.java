package gift.service;

import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;
import gift.entity.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = new Product();
        product.updateFromProductRequestDTO(dto);
        return new ProductResponseDTO(productRepository.create(product));
    }

    @Transactional
    public Optional<ProductResponseDTO> update(Long id, ProductRequestDTO dto) {
        return productRepository.findById(id).map(product -> {
            product.updateFromProductRequestDTO(dto);
            return productRepository.update(product);
        }).map(ProductResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ProductResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
