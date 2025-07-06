package gift.service;

import gift.entity.ProductStatus;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductStatusService {

    private static final List<String> APPROVAL_REQUIRED_WORDS = List.of("카카오");

    public ProductStatus getProductStatus(String productName) {
        if (APPROVAL_REQUIRED_WORDS.stream().anyMatch(productName::contains)) {
            return ProductStatus.PENDING_APPROVAL;
        }

        return ProductStatus.APPROVED;
    }
}
