package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    // 위시리스트 추가
    public WishResponseDto addWish(Member member, WishRequestDto requestDto) {
        // 상품 존재 여부 확인
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 이미 위시리스트에 있는 상품인지 확인
        if (wishRepository.existsByMemberIdAndProductId(member.getId(), requestDto.getProductId())) {
            throw new IllegalArgumentException("이미 위시리스트에 추가된 상품입니다.");
        }

        // 위시리스트 추가
        Wish wish = new Wish(member.getId(), requestDto.getProductId(), requestDto.getQuantity());
        Wish savedWish = wishRepository.save(wish);

        return new WishResponseDto(savedWish, new ProductResponseDto(product));
    }
} 