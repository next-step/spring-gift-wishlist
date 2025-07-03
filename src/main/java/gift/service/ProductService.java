package gift.service;

import gift.entity.Product;
import gift.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, Integer price, String imageUrl) {
        Product product = new Product(name, price, imageUrl, true);
        if (product.getName().contains("카카오")) {
            product = product.updateValidated(false);
        }
        Optional<Long> optionalId = productRepository.saveNewProduct(product);
        if (optionalId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Product creation failed");
        }
        return product.updateId(optionalId.get());
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.getProductById(id);
        throwNotFoundIfTrue(optionalProduct.isEmpty());
        return optionalProduct.get();
    }

    public List<Product> getProductList(Boolean visibility) {
        return productRepository.getProductList(visibility);
    }

    public Product updateSelectivelyProductById(Long id, String name, Integer price, String imageUrl) {
        Optional<Product> optionalProduct = productRepository.getProductById(id);
        throwNotFoundIfTrue(optionalProduct.isEmpty());
        if (name == null && price == null && imageUrl == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 요소가 한개 이상은 작성되어야 합니다");
        }
        Product product = optionalProduct.get();
        if (name != null) {
            product = product.updateName(name);
        }
        if (price != null) {
            product = product.updatePrice(price);
        }
        if (imageUrl != null) {
            product = product.updateImageUrl(imageUrl);
        }
        if (product.getName().contains("카카오")) {
            product = product.updateValidated(false);
        }
        throwNotFoundIfTrue(!productRepository.updateProduct(product));
        return product;
    }

    public Product updateProductById(Long id, String name, Integer price, String imageUrl) {
        Product product = new Product(id, name, price, imageUrl, true);
        if (product.getName().contains("카카오")) {
            product = product.updateValidated(false);
        }
        throwNotFoundIfTrue(!productRepository.updateProduct(product));
        return product;
    }

    public void setProductValidated(Long id, Boolean validated) {
        throwNotFoundIfTrue(!productRepository.setProductValidatedById(id, validated));
    }

    public void deleteProductById(Long id) {
        throwNotFoundIfTrue(!productRepository.deleteProductById(id));
    }

    private void throwNotFoundIfTrue(boolean condition) {
        if (condition) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
