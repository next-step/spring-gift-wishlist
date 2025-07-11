package gift.domain;

public class WishSummary {
    private String productName;
    private int count;

    public WishSummary(String productName, int count) {
        this.productName = productName;
        this.count = count;
    }

    public static WishSummary of(String productName, int count) {
        return new WishSummary(productName, count);
    }

    public String getProductName() {
        return productName;
    }

    public int getCount() {
        return count;
    }
}
