package gift.service;

import gift.domain.Product;
import gift.dto.request.ProductReqDTO;
import gift.dto.response.ProductResDTO;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResDTO save(ProductReqDTO productReqDTO) {
        return convertToDTO(
            productRepository.save(
                new Product(
                    productReqDTO.name(),
                    productReqDTO.price(),
                    productReqDTO.imageURL()
                )
            )
        );
    }

    public ProductResDTO findById(Long id) {
        return convertToDTO(
            productRepository.findById(id)
        );
    }

    public List<ProductResDTO> findAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .toList();
    }

    public ProductResDTO update(Long id, ProductReqDTO productReqDTO) {
        Product product = new Product(
            productReqDTO.name(),
            productReqDTO.price(),
            productReqDTO.imageURL()
        );

        productRepository.update(id, product);

        return convertToDTO(
            productRepository.findById(id)
        );
    }

    public void delete(Long id) {
        productRepository.delete(id);
    }

    private ProductResDTO convertToDTO(Product product) {
        return new ProductResDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageURL()
        );
    }
}
