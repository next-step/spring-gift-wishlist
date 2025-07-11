package gift.service.wishlist;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishList;
import gift.dto.wishlist.WishListRequest;
import gift.dto.wishlist.WishListResponse;
import gift.repository.member.MemberRepository;
import gift.repository.product.ProductRepository;
import gift.repository.wishlist.WishListRepository;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishListService(WishListRepository wishListRepository, MemberRepository memberRepository,
        ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    // wishList 조회
    public List<WishListResponse> findWishListAllById(Long memberId) {
        List<WishList> list = wishListRepository.findWishListAllById(memberId);

        return list.stream()
            .map(each -> {
                Member member = memberRepository.findById(each.getMemberId());
                Product product = productRepository.findById(each.getProductId());

                return new WishListResponse(
                    member.getId(),
                    member.getEmail(),
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    each.getQuantity(),
                    product.getPrice() * each.getQuantity()
                );
            }).toList();

    }

    public void update(Long memberId, WishListRequest wishListRequest) {
        try {
            Long wishListId = wishListRepository.findWishListByUpdateRequest(memberId,
                wishListRequest.productId()).getId();

            wishListRepository.updateWishList(wishListId, wishListRequest.quantity());
        } catch (DataAccessException e) {
            // 만약 찾는 위시리스트가 없으면 생성부터 해야 함.
            wishListRepository.insertWishList(memberId, wishListRequest.productId(),
                wishListRequest.quantity());
        }


    }
}
