package gift.controller;

import gift.dto.request.ApprovedProductCreateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin")
public class ApprovedProductController {

    private final JdbcTemplate jdbcTemplate;

    public ApprovedProductController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/approved-products")
    public ResponseEntity<Void> addApprovedProduct(
        @Valid @RequestBody ApprovedProductCreateRequestDto approvedProductCreateRequestDto) {

        String approvedProductName = approvedProductCreateRequestDto.name();

        String sql = "SELECT COUNT(*) FROM approved_product_names WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
            approvedProductName);

        if (count != null && count > 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "해당 상품명은 이미 등록되어 있습니다."
            );
        }

        String insertSql = "INSERT INTO approved_product_names(name) VALUES(?)";
        jdbcTemplate.update(insertSql, approvedProductName);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
