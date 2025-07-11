package gift.service;

import gift.dto.WishListRequestDto;
import gift.dto.WishListResponseDto;
import gift.entity.WishList;
import gift.repository.WishListRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public List<WishListResponseDto> getWishListByMemberId(Long memberId) {
        List<WishList> wishLists = wishListRepository.getWishListByMemberId(memberId);
        List<WishListResponseDto> wishListResponseDtoList = wishLists.stream()
                .map(wishList -> new WishListResponseDto(wishList.getId(),wishList.getProductId(),wishList.getQuantity()))
                .toList();
        return wishListResponseDtoList;
    }

    @Override
    public void addWishList(Long memberId, WishListRequestDto wishListRequestDto) {
        wishListRepository.addWishList(memberId, wishListRequestDto.getProductId(), wishListRequestDto.getQuantity());

    }

    @Override
    public void deleteWishList(Long memberId, Long wishListId) throws AccessDeniedException {
        validateWishListByMemberIdAndWishListId(memberId, wishListId);
        wishListRepository.deleteWishList(wishListId);
    }

    @Override
    public void validateWishListByMemberIdAndWishListId(Long memberId, Long wishListId) throws AccessDeniedException {
        if(!wishListRepository.isWishListExistByMemberIdAndWishListId(memberId, wishListId)) {
            throw new AccessDeniedException("해당 리소스에 접근할 권한이 없습니다.");
        }
    }
}
