package gift.wishlist;

import java.util.UUID;

public class Wishlist {
    private Long id;

    private UUID user_id;

    private UUID product_id;

    public Wishlist() {}

    public Wishlist(Long id, UUID user_id, UUID product_id) {
        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
    }

    public Long getId() {
        return id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public UUID getProduct_id() {
        return product_id;
    }
}
