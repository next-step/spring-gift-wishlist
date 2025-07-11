package gift.repository;

import gift.dto.WishResponseDTO;
import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Wish> wishRowMapper = (rs, rowNum) -> new Wish(
        rs.getLong("member_id"),
        rs.getLong("product_id"),
        rs.getInt("quantity")
    );

    private final RowMapper<WishResponseDTO> wishResponseDTORowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("p_id"));
        product.setName(rs.getString("p_name"));
        product.setPrice(rs.getLong("p_price"));
        product.setImageUrl(rs.getString("p_image_url"));
        return new WishResponseDTO(rs.getLong("member_id"), product.getProductResponseDTO(), rs.getInt("w_quantity"));
    };

    public void save(Wish wish) {
        String sql = "INSERT INTO wish (member_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, wish.getMemberId(), wish.getProductId(), wish.getQuantity());
    }

    public List<WishResponseDTO> findByMemberIdWithPagination(Long memberId, int limit, long offset, String sort) {
        String sql = "SELECT w.member_id, w.quantity as w_quantity, p.id as p_id, p.name as p_name, p.price as p_price, p.image_url as p_image_url " +
            "FROM wish w " +
            "JOIN product p ON w.product_id = p.id " +
            "WHERE w.member_id = ? " +
            createOrderByClause(sort) + // 정렬 절 생성
            "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, wishResponseDTORowMapper, memberId, limit, offset);
    }

    public Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT member_id, product_id, quantity FROM wish WHERE member_id = ? AND product_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, wishRowMapper, memberId, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateQuantity(Long memberId, Long productId, int quantity) {
        String sql = "UPDATE wish SET quantity = ? WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, quantity, memberId, productId);
    }

    public void deleteByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "DELETE FROM wish WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    private String createOrderByClause(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return "";
        }

        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        String direction = (sortParams.length > 1) ? sortParams[1].toUpperCase() : "ASC";

        String column = switch (property) {
            case "name" -> "p.name";
            case "price" -> "p.price";
            default -> "p.id";
        };

        if (!direction.equals("ASC") && !direction.equals("DESC")) {
            direction = "ASC";
        }
        return "ORDER BY " + column + " " + direction + " ";
    }
}