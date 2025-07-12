package gift.service;

import gift.domain.Member;
import gift.domain.Wish;
import gift.dto.request.WishRequest;
import gift.dto.response.WishAddResponse;
import gift.dto.response.WishResponse;
import gift.exception.ProductNotFoundException;
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
    public WishAddResponse add(Member member, WishRequest request){

        productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException(request.productId()));

        wishRepository.add(new Wish(member.getId(), request.productId()));
        return new WishAddResponse("위시리스트에 추가되었습니다.");
    }

    @Override
    public List<WishResponse> getWishList(Member member) {
        return wishRepository.findAllByMemberId(member.getId()).stream()
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
}
