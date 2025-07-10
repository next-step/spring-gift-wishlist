package com.example.demo.repository;

import com.example.demo.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  Product addProduct(Product product);

  List<Product> productFindAll();

  Optional<Product> productFindById(Long id);

  void productUpdateById(Product product);

  void deleteProductById(Long id);
}
