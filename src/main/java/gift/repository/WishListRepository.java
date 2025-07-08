package gift.repository;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.WishList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class WishListRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishListRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //TODO: WishList에 대한 CRUD
    //조회
    public List<WishResponseDto> getWishList(Long id){
        String sql = "select p.name, p.image_url, w.quantity, p.price from Products p, WishList w, Member m "
                + "where w.memberid = m.id and w.productid = p.id and m.id = ?";
        List<WishResponseDto> mywishList = jdbcTemplate.query(sql, wishResponseDtoRowMapper(), id);
        return mywishList;
    }


    private RowMapper<WishResponseDto> wishResponseDtoRowMapper(){
        return new RowMapper<WishResponseDto>() {
            @Override
            public WishResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                String productName = rs.getString("name");
                String image_url = rs.getString("image_url");
                Integer quantity = rs.getInt("quantity");
                Integer price = rs.getInt("price");
                return new WishResponseDto(productName, image_url, quantity, price);
            }
        };
    }


}
