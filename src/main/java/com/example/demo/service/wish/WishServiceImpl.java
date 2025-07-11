package com.example.demo.service.wish;

import com.example.demo.dto.wish.WishResponseDto;
import com.example.demo.exception.DuplicateWishException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.WishNotFoundException;
import com.example.demo.repository.wish.WishRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService{

  private final WishRepository wishRepository;

  public WishServiceImpl(WishRepository wishRepository) {
    this.wishRepository = wishRepository;
  }

  @Override
  public void saveWishProduct(Long userId, Long productId) {
    if (!wishRepository.existProductById(productId)) {
      throw new ProductNotFoundException("해당 상품은 존재하지 않습니다.");
    }

    if (!wishRepository.existUserById(userId)) {
      throw new UserNotFoundException("해당 유저가 존재하지 않습니다.");
    }

    if (wishRepository.existWish(userId, productId)) {
      throw new DuplicateWishException("이미 찜한 상품입니다.");
    }

    wishRepository.saveWishProduct(userId, productId);
  }

  @Override
  public void deleteWishProduct(Long userId, Long productId) {
    if (!wishRepository.existWish(userId, productId)) {
      throw new WishNotFoundException("해당 상품은 위시리스트에 존재하지 않습니다.");
    }
    wishRepository.deleteWishProduct(userId, productId);

  }

  @Override
  public List<WishResponseDto> getWishProductList(Long userId) {
    return wishRepository.getWishProductList(userId);
  }
}
