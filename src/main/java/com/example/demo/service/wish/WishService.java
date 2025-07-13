package com.example.demo.service.wish;

import com.example.demo.dto.wish.WishResponseDto;
import java.util.List;

public interface WishService {

  void saveWishProduct(Long userId, Long productId);
  void deleteWishProduct(Long userId, Long productId);
  List<WishResponseDto> getWishProductList(Long userId);

}
