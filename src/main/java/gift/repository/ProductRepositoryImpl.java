package gift.repository;

import gift.dto.api.ProductResponseDto;
import gift.entity.Product;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    //DB
    private final JdbcClient products;
    
    public ProductRepositoryImpl(JdbcClient products) {
        this.products = products;
    }
    
    @Override
    public ProductResponseDto addProduct(Product product) {
        var sql = """
            insert into products(name, price, imageUrl)
            values (:name, :price, :imageUrl);
            """;
        GeneratedKeyHolder generatedKey = new GeneratedKeyHolder(); //auto increment로 생성된 key 불러오기
        
        products.sql(sql)
            .param("name", product.getName())
            .param("price", product.getPrice())
            .param("imageUrl", product.getImageUrl())
            .update(generatedKey);
        
        Long recentKey = generatedKey.getKey().longValue();
        
        return new ProductResponseDto(recentKey, product.getName(), product.getPrice(),
            product.getImageUrl());
    }
    
    @Override
    public List<ProductResponseDto> findAllProducts() {
        var sql = """
            select id, name, price, imageUrl from products;
            """;
        
        return products.sql(sql)
            .query((rs, rowNum) -> new ProductResponseDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("price"),
                rs.getString("imageUrl")
            )).list();
    }
    
    @Override
    public Product findProductWithId(Long id) {
        var sql = """
            select id, name, price, imageUrl from products where id = :id;
            """;
        
        return products.sql(sql)
            .param("id", id)
            .query((rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("price"),
                rs.getString("imageUrl")
            ))
            .optional()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    @Override
    public ProductResponseDto modifyProductWithId(Long id, Product newProduct) {
        var sql = """
            update products set name = :name, price = :price, imageUrl = :imageUrl where id = :id;
            """;
        
        products.sql(sql)
            .param("id", newProduct.getId())
            .param("name", newProduct.getName())
            .param("price", newProduct.getPrice())
            .param("imageUrl", newProduct.getImageUrl())
            .update();
        
        return new ProductResponseDto(newProduct);
    }
    
    @Override
    public void deleteProductWithId(Long id) {
        var sql = """
            delete from products where id = :id;
            """;
        
        products.sql(sql)
            .param("id", id)
            .update();
    }
}
