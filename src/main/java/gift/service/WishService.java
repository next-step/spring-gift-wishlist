package gift.service;

import gift.domain.Product;
import gift.dto.CreateWishRequest;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

    public WishService(ProductRepository productRepository, WishRepository wishRestController) {
        this.productRepository = productRepository;
        this.wishRepository = wishRestController;
    }

    public List<Product> productList() {
        return productRepository.findAll();
    }

    public void addWishProduct(CreateWishRequest request) {
        wishRepository.save(request);
    }
}
