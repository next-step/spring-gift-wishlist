package gift.service;

import gift.Entity.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // if문으로 제어해보기
    public boolean validateProduct(Product product, BindingResult bindingResult) {
        if (product.getName().contains("카카오") && !product.getMDapproved()) {
            bindingResult.rejectValue("name", "forbidden.word", "상품 이름에 '카카오'는 포함할 수 없습니다.");
            return false;
        }
        return true;
    }

    // try catch문으로 제어해보기
    public void validateProductException(Product product) {
        if (product.getName().contains("카카오") && !product.getMDapproved()) {
            throw new IllegalArgumentException("상품 이름에 '카카오'는 포함할 수 없습니다.");
        }
    }

    // Create
    public Product insertProduct(Product product) {
        return productRepository.save(product);
    }

    //Read (전체 상품 조회)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //Read (단건 상품 조회)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
    }

    //Update
    public Product updateProduct(Long id, Product product) {
        Product updatedProduct = getProductById(id);
        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setImageUrl(product.getImageUrl());
        updatedProduct.setMDapproved(product.getMDapproved());
        return productRepository.save(updatedProduct);
    }

    // Delete
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

}
