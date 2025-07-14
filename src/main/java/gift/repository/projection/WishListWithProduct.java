package gift.repository.projection;

import gift.domain.Product;

public record WishListWithProduct(Long id, Long memberId, Integer quantity, Product product) {

}