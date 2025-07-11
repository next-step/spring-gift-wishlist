package gift.entity;

public class WishItem {

    private final Long id;
    private final Product product;
    private final Integer quantity;
    private final Member member;

    public WishItem(Long id, Product product, Integer quantity, Member member) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "WishItem(" + id + ") - quantity: " + quantity;
    }

}
