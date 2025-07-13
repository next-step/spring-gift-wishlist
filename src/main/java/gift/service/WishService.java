package gift.service;

import gift.dto.AuthenticatedMemberDto;
import gift.dto.ProductResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.PermissionDeniedException;
import gift.exception.WishAlreadyExistsException;
import gift.exception.WishNotFoundException;
import gift.repository.WishRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Transactional
    public WishResponseDto addWish(
            AuthenticatedMemberDto authenticatedMemberDto,
            WishRequestDto wishRequestDto) {
        Product product = productService.findProductOrThrow(wishRequestDto.productId());

        if (wishRepository.existsByMemberIdAndProductId(authenticatedMemberDto.id(),
                product.getId())) {
            throw new WishAlreadyExistsException(product.getId());
        }

        Wish wish = new Wish(authenticatedMemberDto.id(), product.getId());
        Long id = wishRepository.saveWish(wish);

        return new WishResponseDto(id, ProductResponseDto.from(product));
    }

    @Transactional(readOnly = true)
    public List<WishResponseDto> getWishlistByMemberId(
            AuthenticatedMemberDto authenticatedMemberDto) {

        List<Wish> wishes = wishRepository.findAllWishesByMemberId(authenticatedMemberDto.id());
        List<Long> productIds = wishes.stream()
                                      .map(Wish::getProductId)
                                      .toList();
        List<ProductResponseDto> products = productService.findProductsByIdsIn(productIds);

        Map<Long, ProductResponseDto> productMap = new LinkedHashMap<>();
        for (ProductResponseDto product : products) {
            productMap.put(product.id(), product);
        }

        return wishes.stream()
                     .map(wish -> {
                         return new WishResponseDto(wish.getId(),
                                 productMap.get(wish.getProductId()));
                     })
                     .toList();
    }

    @Transactional
    public void deleteWishById(
            AuthenticatedMemberDto authenticatedMemberDto,
            Long wishId) {
        Wish wish = wishRepository.findWishById(wishId)
                                  .orElseThrow(() -> new WishNotFoundException(wishId));

        if (!authenticatedMemberDto.id().equals(wish.getMemberId())) {
            throw new PermissionDeniedException("해당 상품을 삭제할 권한이 없습니다.");
        }

        wishRepository.deleteWishById(wishId);
    }
}
