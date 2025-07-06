package gift.fixture;

import gift.entity.product.Product;
import gift.repository.ProductRepository;

public class ProductFixture {

    public static Product save(
            ProductRepository repo,
            String name,
            int price,
            String imageUrl
    ) {
        Product p = Product.of(name, price, imageUrl);
        return repo.save(p);
    }
}
