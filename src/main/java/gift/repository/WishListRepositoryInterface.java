package gift.repository;

import gift.entity.Product;

import java.util.List;

public interface WishListRepositoryInterface {
    List<Product> findAllProductsFromWishListByEmail(String email);

    void addProductToWishListByEmail(String email, Long productId);

    boolean deleteProductFromWishListByEmail(String email, Long productId);
}
