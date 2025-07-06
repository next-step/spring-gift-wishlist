package gift.entity.product;

import gift.dto.ProductResponse;
import gift.entity.product.value.ProductId;
import gift.entity.product.value.ProductImageUrl;
import gift.entity.product.value.ProductName;
import gift.entity.product.value.ProductPrice;

public record Product(
        ProductId id,
        ProductName name,
        ProductPrice price,
        ProductImageUrl imageUrl,
        boolean hidden
) {

    public static Product of(
            Long id,
            String name,
            int price,
            String imageUrl,
            boolean hidden
    ) {
        return new Product(
                new ProductId(id),
                new ProductName(name),
                new ProductPrice(price),
                new ProductImageUrl(imageUrl),
                hidden
        );
    }

    public static Product of(
            Long id,
            String name,
            int price,
            String imageUrl
    ) {
        return new Product(
                new ProductId(id),
                new ProductName(name),
                new ProductPrice(price),
                new ProductImageUrl(imageUrl),
                false
        );
    }

    public static Product of(
            String name,
            int price,
            String imageUrl) {
        return new Product(
                null,
                new ProductName(name),
                new ProductPrice(price),
                new ProductImageUrl(imageUrl),
                false
        );
    }

    public Product withId(Long newId) {
        return new Product(new ProductId(newId), this.name, this.price, this.imageUrl, this.hidden);
    }

    public Product withName(String newName) {
        return new Product(this.id, new ProductName(newName), this.price, this.imageUrl,
                this.hidden);
    }

    public Product withPrice(int newPrice) {
        return new Product(this.id, this.name, new ProductPrice(newPrice), this.imageUrl,
                this.hidden);
    }

    public Product withImageUrl(String newUrl) {
        return new Product(this.id, this.name, this.price, new ProductImageUrl(newUrl),
                this.hidden);
    }

    public Product withHidden(boolean newHidden) {
        return new Product(this.id, this.name, this.price, this.imageUrl, newHidden);
    }

    public ProductResponse toResponse() {
        return new ProductResponse(this.id.value(), this.name.value(), this.price.value(),
                this.imageUrl.value());
    }
}
