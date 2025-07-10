package gift.repository;

import gift.entity.Member;
import gift.entity.Product;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryInterface {

    Optional<Member> findByEmail(String email);

    void save(Member member);

    Optional<Member> findByEmailAndPassword(String email, String password);

    List<Product> findAllProductsFromWishListByEmail(String email);

    void addProductToWishListByEmail(String email, Long productId);

    boolean deleteProductFromWishListByEmail(String email, Long productId);
}
