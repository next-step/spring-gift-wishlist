package gift.service.product;

import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;

  public ProductServiceImpl(ProductRepository repository) {
    this.repository = repository;
  }


  @Override
  public List<Product> findAllProduct() {
    List<Product> allProduct = repository.findAllProduct();
//    List<ProductResponseDto> responseDtoList = new ArrayList<>();
//    for (Product product : allProduct) {
//      ProductResponseDto responseDto = new ProductResponseDto(product);
//      responseDtoList.add(responseDto);
//    }
    return allProduct;
  }

  @Override
  public Product findProductById(Long id) {
    return repository.findProductById(id)
//        .map(ProductResponseDto::new)
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
  }

  @Override
  public Product createProduct(Product requestDto) {
    Product product = repository.createProduct(
        new Product(requestDto.getName(), requestDto.getPrice(),
            requestDto.getImageUrl()));
    return product;
  }

  @Override
  public Product updateProduct(Long id, Product requestDto) {

    return repository.updateProduct(id, new Product(requestDto.getName(), requestDto.getPrice(),
            requestDto.getImageUrl()))
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
  }

  @Override
  public void deleteProduct(Long id) {
    int deletedProduct = repository.deleteProduct(id);

  }
}
