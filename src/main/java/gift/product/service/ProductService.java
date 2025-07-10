package gift.product.service;

import gift.product.domain.Product;
import gift.product.dto.ProductPatchRequestDto;
import gift.product.dto.ProductSaveRequestDto;
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
    public Product saveProduct(ProductSaveRequestDto productSaveRequestDto) {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, productSaveRequestDto.getName(), productSaveRequestDto.getPrice(), productSaveRequestDto.getImageUrl());
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
    public Product updateProduct(UUID id, ProductPatchRequestDto productPatchRequestDto) {
        productdao.findById(id);
        return productdao.update(id, productPatchRequestDto);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productdao.findById(id);
        productdao.delete(id);
    }
}
