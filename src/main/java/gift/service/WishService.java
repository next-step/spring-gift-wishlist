package gift.service;

import gift.repository.ProductJdbcRepository;
import gift.repository.UserJdbcRepository;
import org.springframework.stereotype.Service;

@Service
public class WishService implements WishServiceInterface {

    private final UserJdbcRepository userRepository;
    private final ProductJdbcRepository productRepository;

    public WishService(UserJdbcRepository userRepository, ProductJdbcRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


}
