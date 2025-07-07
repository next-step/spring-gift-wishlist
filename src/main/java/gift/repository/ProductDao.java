package gift.repository;

import gift.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();

    public ProductDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public void insertProduct(Product product) {
        final var sql = "insert into product(name, price, image, mdApproved) values(?, ?, ?,?)";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImage(),product.getMdApproved());
    }
    public List<Product> getAllProducts() {
        final var sql = "select * from product";
        return jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER);
    }
    public Product getProductById(long id) {
        final var sql = "select * from product where id = ?";
        return jdbcTemplate.queryForObject(sql, PRODUCT_ROW_MAPPER,id);
    }
    public void removeProduct(long id) {
        final var sql = "delete from product where id = ?";
        jdbcTemplate.update(sql, id);
    }
    public void updateProduct(Long id, Product product, Product product_changed) {
        final var sql = "UPDATE product SET name = ?, price = ?, image = ?, mdApproved = ? WHERE id = ?";
        product.updateFields(product_changed);
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImage(),product.getMdApproved(),id);
    }
    public static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image"),
                    rs.getBoolean("mdApproved")
            );
        }
    }
}