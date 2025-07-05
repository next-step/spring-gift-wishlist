package gift.service.product;

import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.ProductResponseDto;
import java.util.List;

public interface ProductService {
    
    ProductResponseDto addProduct(AddProductRequestDto requestDto);
    
    List<ProductResponseDto> findAllProducts();
    
    ProductResponseDto findProductWithId(Long id);
    
    ProductResponseDto modifyProductWithId(Long id, ModifyProductRequestDto requestDto);
    
    void deleteProductWithId(Long id);
    
    ProductResponseDto modifyProductInfoWithId(Long id, ModifyProductRequestDto requestDto);
}
