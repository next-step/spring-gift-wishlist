package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;

  public ProductServiceImpl(ProductRepository repository) {
    this.repository = repository;
  }


  @Override
  public List<ProductResponseDto> findAllProduct() {
    return repository.findAllProduct();
  }

  @Override
  public ProductResponseDto findProductById(Long id) {
    return repository.findProductById(id)
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
  }

  @Override
  public ProductResponseDto createProduct(ProductRequestDto requestDto) {
    return repository.createProduct(requestDto);
  }

  @Override
  public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {

    return repository.updateProduct(id, requestDto)
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
  }

  @Override
  public void deleteProduct(Long id) {
    int delete = repository.deleteProduct(id);
    if (delete != 1) {
      throw new ProductNotFoundException("삭제할 것이 없습니다");
    }
  }
}
