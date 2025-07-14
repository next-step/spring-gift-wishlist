package gift.service;

import gift.dto.WishListRequestDto;
import gift.dto.WishListResponseDto;
import gift.entity.Product;
import gift.entity.WishList;
import gift.exception.WishListAccessDeniedException;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListServiceImpl(WishListRepository wishListRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<WishListResponseDto> getWishListByMemberId(Long memberId) {
        List<WishList> wishLists = wishListRepository.getWishListByMemberId(memberId);

        List<WishListResponseDto> wishListResponseDtoList = wishLists.stream()
                .map(wishList -> {
                    // productId로 product 조회
                    Product product = productRepository.findProduct(wishList.getProductId());

                    return new WishListResponseDto(
                            wishList.getId(),
                            wishList.getProductId(),
                            wishList.getQuantity(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl()
                    );
                })
                .toList();
        return wishListResponseDtoList;
    }

    @Override
    public void addWishList(Long memberId, WishListRequestDto wishListRequestDto) {
        wishListRepository.addWishList(memberId, wishListRequestDto.getProductId(), wishListRequestDto.getQuantity());

    }

    @Override
    public void deleteWishList(Long memberId, Long wishListId) {
        validateWishListByMemberIdAndWishListId(memberId, wishListId);
        wishListRepository.deleteWishList(wishListId);
    }

    @Override
    public void validateWishListByMemberIdAndWishListId(Long memberId, Long wishListId){
        if(!wishListRepository.isWishListExistByMemberIdAndWishListId(memberId, wishListId)) {
            throw new WishListAccessDeniedException();
        }
    }
}
