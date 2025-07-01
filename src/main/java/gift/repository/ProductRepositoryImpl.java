package gift.repository;

import gift.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();

    @Override
    public Product addProduct(String name, Long price, String url) {
        Long newId = products.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        Product product = new Product(newId, name, price, url);
        products.put(newId, product);
        return product;
    }

    @Override
    public Product findProductById(Long id) {
        Product product = products.get(id);
        return product;
    }

    @Override
    public List<Product> findAllProduct() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void updateProductById(Product newProduct) {
        products.put(newProduct.id(), newProduct);
    }

    @Override
    public void deleteProductById(Long id) {
        products.remove(id);
    }
}
