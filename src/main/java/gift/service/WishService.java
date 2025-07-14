package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ForbiddenAccessException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        // 위시리스트에 상품 추가
        Wish wish = new Wish(member.getId(), requestDto.getProductId(), requestDto.getQuantity());
        Wish savedWish = wishRepository.save(wish);

        return new WishResponseDto(savedWish, new ProductResponseDto(product));
    }

    // 위시리스트 조회
    public List<WishResponseDto> getWishesByMember(Member member) {
        List<Wish> wishes = wishRepository.findByMemberId(member.getId());
        
        return wishes.stream()
                .map(wish -> {
                    Product product = productRepository.findById(wish.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
                    return new WishResponseDto(wish, new ProductResponseDto(product));
                })
                .collect(Collectors.toList());
    }

    // 위시리스트 수량 변경
    public WishResponseDto updateWishQuantity(Member member, Long wishId, Integer quantity) {
        // 위시리스트 항목 조회
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 해당 ID의 상품이 현재 로그인한 사용자 건지 검증 
        if (!wish.getMemberId().equals(member.getId())) {
            throw new ForbiddenAccessException("권한이 없습니다."); 
        }

        // 요청된 수량으로 Wish.quantity 필드 업데이트
        wishRepository.updateQuantity(wishId, quantity);
        wish.setQuantity(quantity);

        // 상품 정보 조회
        Product product = productRepository.findById(wish.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return new WishResponseDto(wish, new ProductResponseDto(product));
    }

    // 위시리스트 삭제
    public void deleteWish(Member member, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (!wish.getMemberId().equals(member.getId())) {
            throw new ForbiddenAccessException("권한이 없습니다.");
        }

        wishRepository.deleteById(wishId);
    }
} 