package com.example.demo.repository.wish;

import com.example.demo.dto.wish.WishResponseDto;
import java.util.List;

public interface WishRepository {

  public boolean existProductById(Long productId);
  public boolean existUserById(Long userId);
  public void saveWishProduct(Long userId, Long productId);
  public void deleteWishProduct(Long userId, Long productId);
  public List<WishResponseDto> getWishProductList(Long userId);
  public boolean existWish(Long userId, Long productId);
}
