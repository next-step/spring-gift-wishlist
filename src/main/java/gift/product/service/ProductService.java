package gift.product.service;

import gift.global.common.dto.PageRequest;
import gift.global.common.dto.PagedResult;
import gift.product.domain.Product;
import gift.product.dto.CreateProductReqDto;
import gift.product.dto.GetProductResDto;
import gift.product.dto.UpdateProductReqDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import gift.product.validation.ProductValidator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductValidator productValidator;

  public ProductService(ProductRepository productRepository, ProductValidator productValidator) {
    this.productRepository = productRepository;
    this.productValidator = productValidator;
  }

  public PagedResult<GetProductResDto> getAllByPage(PageRequest pageRequest)
      throws IllegalArgumentException {
    List<Product> pagedProductList = productRepository.findAllByPage(pageRequest.offset(),
        pageRequest.pageSize(), pageRequest.sortInfo());
    return PagedResult.of(pagedProductList, pageRequest.offset(), pageRequest.pageSize())
        .map(GetProductResDto::from);
  }

  public GetProductResDto getProductById(Long id) throws ProductNotFoundException {
    Product product = productRepository.findById(id)
        .orElseThrow(ProductNotFoundException::new);
    return GetProductResDto.from(product);
  }

  @Transactional
  public Long createProduct(CreateProductReqDto dto) {
    productValidator.validateProductName(dto.name());
    Product newProduct = Product.of(
        dto.name(),
        dto.price(),
        dto.description(),
        dto.imageUrl()
    );
    return productRepository.save(newProduct);
  }

  @Transactional
  public void updateProduct(Long id, UpdateProductReqDto dto) throws ProductNotFoundException {
    productValidator.validateProductName(dto.name());
    if (productRepository.findById(id).isEmpty()) {
      throw new ProductNotFoundException();
    }
    Product newProduct = Product.withId(
        id,
        dto.name(),
        dto.price(),
        dto.description(),
        dto.imageUrl()
    );
    productRepository.update(id, newProduct);
  }

  @Transactional
  public void deleteProduct(Long id) throws ProductNotFoundException {
    if (productRepository.findById(id).isEmpty()) {
      throw new ProductNotFoundException();
    }
    productRepository.deleteById(id);
  }
}
