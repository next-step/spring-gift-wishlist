package gift.service;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Product;
import gift.entity.WishList;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListService(
            WishListRepository wishListRepository, ProductRepository productRepository
    ) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    public WishResponseDto addToWishList(Long memberId, WishRequestDto requestDto){
        Optional<Product> productOptional = productRepository.findById(requestDto.productId());
        if(productOptional.isEmpty()) {
            throw new ProductNotFoundException("해당 상품은 존재하지 않는 상품입니다.");
        }

        //이미 장바구니에 해당 상품이 있는 경우에는 수량만 업데이트
        if(wishListRepository.checkExistence(memberId, requestDto.quantity(), productOptional.get())){
            return wishListRepository.getWishProduct(memberId, productOptional.get().getId());
        }

        WishList wishList = new WishList(memberId, requestDto.productId(), requestDto.quantity());
        return wishListRepository.add(wishList, productOptional.get());
    }

    public List<WishResponseDto> getList(Long memeberId){
        return wishListRepository.getWishList(memeberId);
    }

    public void removeFromWishList(Long wishListId){
       wishListRepository.remove(wishListId);
    }

    public List<WishResponseDto> changeQuantity(Long memberId, Long wishListId, int amount){
        wishListRepository.updateQuantity(memberId, wishListId, amount);
        List<WishResponseDto> myList = getList(memberId);
        return myList;
    }



}
