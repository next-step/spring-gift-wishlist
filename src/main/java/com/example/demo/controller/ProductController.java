package com.example.demo.controller;

import com.example.demo.dto.ProductRequestDto;
import com.example.demo.dto.ProductResponseDto;
import com.example.demo.dto.ProductUpdateDto;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService){
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<ProductResponseDto> addProduct(
      @Valid @RequestBody ProductRequestDto dto){
    return new ResponseEntity<>(productService.addProduct(dto), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> productFindById(@PathVariable Long id) {
    return new ResponseEntity<>(productService.productFindById(id),HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> productFindAll(){
    return new ResponseEntity<>(productService.productFindAll(), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProductResponseDto> productUpdateById(
      @PathVariable Long id,
      @Valid @RequestBody ProductUpdateDto dto
  ){
    return new ResponseEntity<>(productService.productUpdateById(id, dto), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> productDeleteById(@PathVariable Long id){
    productService.productDeleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
