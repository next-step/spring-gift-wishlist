package gift.service;

import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.request.WishRequest;
import gift.dto.response.WishAddResponse;
import gift.dto.response.WishMsgResponse;
import gift.dto.response.WishResponse;
import gift.exception.ProductNotFoundException;
import gift.exception.WishNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WishServiceImpl implements WishService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

    public WishServiceImpl(ProductRepository productRepository, WishRepository wishRepository){
        this.productRepository = productRepository;
        this.wishRepository = wishRepository;
    }

    @Override
    public WishAddResponse add(Long memberId, WishRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException(request.productId()));

        Wish savedWish = wishRepository.add(new Wish(memberId, request.productId()));

        WishResponse wishResponse = new WishResponse(
                savedWish.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );

        return new WishAddResponse("위시리스트에 추가되었습니다.", wishResponse);
    }

    @Override
    public List<WishResponse> getWishList(Long memberId) {
        return wishRepository.findAllByMemberId(memberId).stream()
                .map(wish -> productRepository.findById(wish.getProductId())
                        .map(product -> new WishResponse(
                                wish.getId(),
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getImageUrl()
                        ))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .toList();
    }


    @Override
    public WishMsgResponse deleteByProductId(Long memberId, Long productId) {
        Wish wish = wishRepository.findByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new WishNotFoundException(productId));

        wishRepository.delete(wish.getId());

        return new WishMsgResponse("위시리스트에서 삭제되었습니다.");
    }
}
