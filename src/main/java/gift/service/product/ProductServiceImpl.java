package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.product.ProductRepository;
import java.util.ArrayList;
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
    List<Product> allProduct = repository.findAllProduct();
    List<ProductResponseDto> responseDtoList = new ArrayList<>();
    for (Product product : allProduct) {
      ProductResponseDto responseDto = new ProductResponseDto(product);
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }

  @Override
  public ProductResponseDto findProductById(Long id) {
    return repository.findProductById(id)
        .map(ProductResponseDto::new)
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
  }

  @Override
  public ProductResponseDto createProduct(ProductRequestDto requestDto) {
    Product product = repository.createProduct(
        new Product(requestDto.getName(), requestDto.getPrice(),
            requestDto.getImageUrl()));
    return new ProductResponseDto(product);
  }

  @Override
  public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
    return repository.updateProduct(id,
            new Product(requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl()))
        .map(ProductResponseDto::new)
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
  }

  @Override
  public void deleteProduct(Long id) {
    int deletedProduct = repository.deleteProduct(id);
  }
}
