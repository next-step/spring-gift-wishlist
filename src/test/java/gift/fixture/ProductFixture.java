// src/test/java/gift/fixture/ProductFixture.java
package gift.fixture;

import gift.entity.product.Product;

public final class ProductFixture {

    private ProductFixture() {
    }

    public static Product create(
            Long id,
            String name,
            int price,
            String imageUrl,
            boolean hidden
    ) {
        // Product.of(id, name, price, imageUrl, hidden) 팩토리 메서드가 존재한다고 가정
        return Product.of(id, name, price, imageUrl, hidden);
    }

    public static Product visible(
            Long id,
            String name,
            int price,
            String imageUrl
    ) {
        return create(id, name, price, imageUrl, false);
    }

    public static Product hidden(
            Long id,
            String name,
            int price,
            String imageUrl
    ) {
        return create(id, name, price, imageUrl, true);
    }
}
