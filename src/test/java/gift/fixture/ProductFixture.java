package gift.fixture;

import gift.dto.request.ProductRequestDto;
import gift.entity.Product;

public class ProductFixture {
    public static ProductRequestDto createProduct() {
        return new ProductRequestDto("테스트상품", 5000, "https://image.url");
    }

}
