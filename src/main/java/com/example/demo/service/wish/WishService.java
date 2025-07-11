package com.example.demo.service.wish;

import com.example.demo.dto.wish.WishResponseDto;
import java.util.List;

public interface WishService {

  public void saveWishProduct(Long userId, Long productId);
  public void deleteWishProduct(Long userId, Long productId);
  public List<WishResponseDto> getWishProductList(Long userId);

}
