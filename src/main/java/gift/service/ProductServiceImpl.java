package gift.service;

import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto) {
        Product newProduct;
        if (requestDto.name().contains("카카오")) {
            newProduct = new Product(null, requestDto.name(), requestDto.price(),
                    requestDto.imageUrl(), false);
        } else {
            newProduct = new Product(null, requestDto.name(), requestDto.price(),
                    requestDto.imageUrl(), true);
        }
        Product savedProduct = productRepository.createProduct(newProduct);

        return new ProductResponseDto(savedProduct.getId(), savedProduct.getName(),
                savedProduct.getPrice(), savedProduct.getImageUrl());
    }

    @Override
    public List<ProductResponseDto> findAllProducts() {

        List<Product> products = productRepository.findAllProducts();
        List<ProductResponseDto> productsList = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDto responseDto = new ProductResponseDto(product.getId(),
                    product.getName(), product.getPrice(), product.getImageUrl());
            productsList.add(responseDto);
        }
        return productsList;
    }

    @Override
    public ProductResponseDto findProductById(Long id) {
        Product find = findProductByIdOrElseThrow(id);

        if (!find.getAcceptedByMD()) {
            throw new CustomException(ErrorCode.Forbidden);
        }

        ProductResponseDto responseDto = new ProductResponseDto(find.getId(), find.getName(),
                find.getPrice(), find.getImageUrl());
        return responseDto;
    }

    @Override
    public ProductResponseDto updateProductById(Long id, CreateProductRequestDto requestDto) {
        Product find = findProductByIdOrElseThrow(id);
        Boolean accepted;

        if (requestDto.name().contains("카카오") || find.getAcceptedByMD() == false) {
            accepted = false;
        } else {
            accepted = true;
        }

        Product newProduct = new Product(id, requestDto.name(), requestDto.price(),
                requestDto.imageUrl(), accepted);

        productRepository.updateProductById(id, newProduct);
        Product updated = findProductByIdOrElseThrow(id);
        return new ProductResponseDto(updated.getId(), updated.getName(), updated.getPrice(),
                updated.getImageUrl());
    }

    @Override
    public void deleteProductById(Long id) {
        findProductByIdOrElseThrow(id);
        productRepository.deleteProductById(id);
    }

    @Override
    public void acceptedProductById(Long id) {
        findProductByIdOrElseThrow(id);
        productRepository.acceptedProductById(id);
    }

    private Product findProductByIdOrElseThrow(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
