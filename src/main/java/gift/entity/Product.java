package gift.entity;


public record Product(
    long productId,
    String name,
    int price,
    String imageURL) {

    public Product {
        if (!name.matches("^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$")) {
            throw new IllegalArgumentException("15자 이내의 한글, 영문, (), [], +, -, &, /, _만 사용할 수 있습니다");
        }

        if (price >= 1000000000 || price < 0) {
            throw new IllegalArgumentException("가격은 1 ~ 1000000000 정수만 가능합니다");
        }
    }

}