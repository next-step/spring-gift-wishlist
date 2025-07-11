package gift.service.wish;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ProductNotFoundException;
import gift.exception.WishListNotFoundException;
import gift.repository.product.ProductRepository;
import gift.repository.wish.WishRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

  private WishRepository wishRepository;

  private ProductRepository productRepository;


  public WishServiceImpl(WishRepository wishRepository, ProductRepository productRepository) {
    this.wishRepository = wishRepository;
    this.productRepository = productRepository;
  }

  @Override
  public List<WishResponseDto> findByMemberId(Long memberId) {
    List<Wish> allWish = wishRepository.findByMemberId(memberId);
    List<WishResponseDto> responseDtoList = new ArrayList<>();
    for (Wish wish : allWish) {
      WishResponseDto responseDto = new WishResponseDto(wish.getId(), wish.getMemberId(),
          wish.getProductId(), wish.getQuantity());
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }

  @Override
  public WishResponseDto createWish(Long memberId, WishRequestDto requestDto) {

    Optional<Product> productById = productRepository.findProductById(requestDto.getProductId());
    if (productById.isEmpty()) {
      throw new ProductNotFoundException("위시 리스트에 넣으려는 상품이 없습니다.");
    }

    Wish wish = wishRepository.createWish(
        new Wish(memberId, requestDto.getProductId(),
            requestDto.getQuantity()));
    return new WishResponseDto(wish.getId(), wish.getMemberId(), wish.getProductId(),
        wish.getQuantity());
  }

  @Override
  public WishResponseDto updateQuantity(Long memberId, WishRequestDto requestDto) {
    Optional<Product> productById = productRepository.findProductById(requestDto.getProductId());
    if (productById.isEmpty()) {
      throw new ProductNotFoundException("위시 리스트에 넣으려는 상품이 없습니다.");
    }
    Optional<Wish> wish = wishRepository.updateQuantity(memberId,
        new Wish(memberId, requestDto.getProductId(),
            requestDto.getQuantity()));
    return wishRepository.updateQuantity(memberId,
            new Wish(memberId, requestDto.getProductId(),
                requestDto.getQuantity()))
        .map(WishResponseDto::new)
        .orElseThrow(() -> new WishListNotFoundException("업데이트 하려는 위시 리스트가 없습니다."));
  }

  @Override
  public void deleteByMemberId(Long memberId) {
    wishRepository.deleteByMemberId(memberId);
  }
}
