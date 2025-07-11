package com.example.demo.repository;

import com.example.demo.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final JdbcClient jdbcClient;

  public ProductRepositoryImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public Product addProduct(Product product) {
    String sql = "INSERT INTO product (name, price, image_url) VALUES (:name, :price, :imageUrl)";

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcClient.sql(sql)
              .param("name", product.getName())
              .param("price", product.getPrice())
              .param("imageUrl", product.getImageUrl())
              .update(keyHolder);

    if (keyHolder.getKeys() != null) {
      product.setId(keyHolder.getKey().longValue());
    }
    return product;
  }

  @Override
  public List<Product> productFindAll() {
    String sql = "SELECT id, name, price, image_url AS imageUrl FROM product";
    return jdbcClient.sql(sql)
                     .query(Product.class)
                     .list();
  }

  @Override
  public Optional<Product> productFindById(Long id) {
    String sql = "SELECT id, name, price, image_url AS imageUrl FROM product WHERE id = :id";
    return jdbcClient.sql(sql)
                     .param("id", id)
                     .query(Product.class)
                     .optional();
  }

  @Override
  public void productUpdateById(Product product) {
    String sql = "UPDATE product SET name = :name, price = :price, image_url = :imageUrl WHERE id = :id";
    jdbcClient.sql(sql)
              .param("name", product.getName())
              .param("price", product.getPrice())
              .param("imageUrl", product.getImageUrl())
              .param("id", product.getId())
              .update();
  }

  @Override
  public void deleteProductById(Long id) {
    String sql = "DELETE FROM product WHERE id = :id";
    jdbcClient.sql(sql)
              .param("id", id)
              .update();
  }
}