package gift.product.adapter.web;

import gift.common.annotation.Adapter;
import gift.common.pagination.Page;
import gift.common.pagination.Pageable;
import gift.product.application.port.in.*;
import gift.product.application.port.in.dto.ProductRequest;
import gift.product.application.port.in.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Adapter
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final AddProductUseCase addProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(AddProductUseCase addProductUseCase, GetProductUseCase getProductUseCase,
                             GetAllProductsUseCase getAllProductsUseCase, UpdateProductUseCase updateProductUseCase,
                             DeleteProductUseCase deleteProductUseCase) {
        this.addProductUseCase = addProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = addProductUseCase.addProduct(productRequest);
        return ResponseEntity.created(URI.create("/api/products/" + productResponse.id())).build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(Pageable pageable) {
        Page<ProductResponse> productResponses = getAllProductsUseCase.getProducts(pageable);
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse productResponse = getProductUseCase.getProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProductRequest productRequest) {
        updateProductUseCase.updateProduct(id, productRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        deleteProductUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
} 