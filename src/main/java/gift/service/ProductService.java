package gift.service;

import gift.dto.product.ProductRequestDto;
import gift.model.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    private final String IMAGE_BASE_URL = "https://media.istockphoto.com/id/1667499762/ko/%EB%B2%A1%ED%84%B0/%EC%98%81%EC%97%85%EC%A4%91-%ED%8C%90%EC%A7%80-%EC%83%81%EC%9E%90.jpg?s=612x612&w=0&k=20&c=94uRFQLclgFtnDhE4OfO1tCJdETL3uuBM9ZHD_N4P4Y=";

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public List<ProductRequestDto> findAllProducts() {

        return productRepository.findAll();
    }

    public ProductRequestDto findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("상품이 없습니다"));
    }

    public void createProduct(ProductRequestDto productDto) {
        Product product;
        if(productDto.getImageUrl()==null || productDto.getImageUrl().isEmpty()) {
            product = new Product(productDto.getName(),
                    productDto.getPrice(),productDto.getUsableKakao());
            product.setImageUrl(IMAGE_BASE_URL);
        } else {
            product = new Product(productDto.getName(), productDto.getPrice(),
                    productDto.getUsableKakao(), productDto.getImageUrl());
        }
        productRepository.save(product);
        productDto.setId(product.getId());
    }

    public void updateProduct(ProductRequestDto productDto) {
        productRepository.update(productDto);
    }

    public void deleteProduct(Long id) {
        productRepository.delete(id);
    }
}
