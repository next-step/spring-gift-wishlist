package gift.repository;

import gift.entity.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getInt("price"));
        product.setImageUrl(rs.getString("image_url"));
        return product;
    };

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            // 새로운 상품 등록
            String sql = "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setInt(2, product.getPrice());
                ps.setString(3, product.getImageUrl());
                return ps;
            }, keyHolder);

            product.setId(keyHolder.getKey().longValue());
        } else {
            // 기존 상품 수정
            String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
            jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), product.getId());
        }
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT id, name, price, image_url FROM product WHERE id = ?";
        List<Product> results = jdbcTemplate.query(sql, productRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT id, name, price, image_url FROM product ORDER BY id";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Product> findPage(int page, int size) {
        String sql = "SELECT id, name, price, image_url FROM product ORDER BY id LIMIT ? OFFSET ?";
        int offset = page * size;
        return jdbcTemplate.query(sql, productRowMapper, size, offset);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM product";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
