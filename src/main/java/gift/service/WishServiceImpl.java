package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.WishRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService{
    private final WishRepository wishRepository;

    private final ProductService productService;

    public WishServiceImpl(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Override
    public WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId) {
        ProductResponseDto productResponseDto = productService.findProductById(requestDto.productId());
        Wish newWish = new Wish(null,requestDto.productId(), memberId, requestDto.quantity());
        Wish savedWish = wishRepository.createWish(newWish);
        return new WishResponseDto(productResponseDto, savedWish.getQuantity());
    }

    @Override
    public List<WishResponseDto> findMemberWishes(Long memberId) {

        List<Wish> wishes = wishRepository.findMemberWishes(memberId);
        List<WishResponseDto> wishesList = new ArrayList<>();
        for (Wish wish : wishes) {
            Long productId = wish.getProductId();
            ProductResponseDto productResponseDto = productService.findProductById(productId);
            WishResponseDto responseDto = new WishResponseDto(productResponseDto, wish.getQuantity());
            wishesList.add(responseDto);
        }
        return wishesList;
    }
}