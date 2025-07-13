package gift.service;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.DuplicateWishException;
import gift.exception.InsertFailedException;
import gift.exception.UpdateFailedException;
import gift.repository.H2ProductRepository;
import gift.repository.H2WishRepository;
import gift.repository.record.WishProductView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishService {

    private final H2WishRepository wishRepository;
    private final H2ProductRepository productRepository;

    public WishService(H2WishRepository wishRepository, H2ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public List<WishResponseDto> getAllWishes(Long userId) {
        List<WishProductView> wishlist = wishRepository.findAllByUserId(userId);

        return wishlist.stream()
                    .map(row -> new WishResponseDto(row.productId(), row.productName(), row.quantity()))
                    .toList();
    }

    public WishResponseDto createWish(Long userId, WishRequestDto wishRequestDto) {
        Wish wishRequest = wishRequestDto.toEntity();
        wishRequest.setUserId(userId);

        // 이미 wish가 있을 경우
        if (wishRepository.isWishExist(wishRequest.getUserId(), wishRequest.getProductId())) {
            throw new DuplicateWishException("이미 동일한 상품이 존재합니다.");
        }

        Product product = productRepository.findById(wishRequest.getProductId())
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다. " +
                                                              "productId = " + wishRequest.getProductId()));

        wishRepository.save(wishRequest)
                .orElseThrow(() -> new InsertFailedException("저장 실패"));

        return new WishResponseDto(product.getId(), product.getName(), wishRequest.getQuantity());
    }

    public WishResponseDto updateWish(Long userId, WishRequestDto wishRequestDto) {
        Wish wishRequest = wishRequestDto.toEntity();
        wishRequest.setUserId(userId);

        Product product = productRepository.findById(wishRequest.getProductId())
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다. " +
                                                              "productId = " + wishRequest.getProductId()));

        wishRepository.update(wishRequest)
                .orElseThrow(() -> new UpdateFailedException("수정할 wish 없음."));

        return new WishResponseDto(product.getId(), product.getName(), wishRequest.getQuantity());
    }

    public void deleteWish(Long userId, Long productId) {
        boolean success = wishRepository.deleteById(userId, productId);

        if (!success) {
            throw new NoSuchElementException("삭제할 wish 없음.");
        }
    }
}
