package gift.product.service;

import gift.product.domain.Product;
import gift.product.dto.RequestDto;
import gift.product.repository.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductDao productdao;

    public ProductService(ProductDao productdao) {
        this.productdao = productdao;
    }

    @Transactional
    public Product saveProduct(RequestDto requestDto) {
        if(requestDto.getPrice() < 0) {
            throw new IllegalArgumentException("Price should be positive");
        }
        UUID id = UUID.randomUUID();
        Product product = new Product(id, requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
        return productdao.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productdao.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(UUID id) {
        return productdao.findById(id);
    }

    @Transactional
    public Product updateProduct(UUID id, RequestDto requestDto) {
        if(requestDto.getPrice() < 0) {
            throw new IllegalArgumentException("Price should be positive");
        }
        productdao.findById(id);
        return productdao.update(id, requestDto);

    }

    @Transactional
    public void deleteProduct(UUID id) {
        productdao.findById(id);
        productdao.delete(id);
    }
}
