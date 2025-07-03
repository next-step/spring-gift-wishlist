package gift.service;

import gift.dto.ProductAdminRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import java.util.List;

public interface ProductService {

  ProductResponseDto createProduct(ProductRequestDto productRequestDto);

  ProductResponseDto createAdminProduct(ProductAdminRequestDto productAdminRequestDto);

  List<ProductResponseDto> searchAllProducts();

  ProductResponseDto searchProductById(Long id);

  ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

  ProductResponseDto updateAdminProduct(Long id, ProductAdminRequestDto productAdminRequestDto);

  void deleteProduct(Long id);
}
