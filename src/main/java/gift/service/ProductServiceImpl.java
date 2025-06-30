package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
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
    return repository.findProductById(id);
  }

  @Override
  public ProductResponseDto createProduct(ProductRequestDto requestDto) {
    return repository.createProduct(requestDto);
  }

  @Override
  public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {

    if (repository.findProductById(id) == null) {
      throw new IllegalStateException("업데이트하려는 상품이 없습니다");
    }
    return repository.updateProduct(id, requestDto);
  }

  @Override
  public void deleteProduct(Long id) {
    repository.deleteProduct(id);
  }
}
