package gift.repository;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Product;
import gift.entity.WishList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class WishListRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishListRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public WishResponseDto add(WishList wishList, Product product){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("wishlist").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("memberid", wishList.getMemberId());
        params.put("productid", wishList.getProductId());
        params.put("quantity", wishList.getQuantity());

        Long wishListId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        Integer totalPrice = product.getPrice() * wishList.getQuantity();
        WishResponseDto wishResponseDto = new WishResponseDto(wishListId, product.getName(), product.getImageUrl(), wishList.getQuantity(), totalPrice);

        return wishResponseDto;
    }

    //TODO: WishList에 대한 CRUD
    //조회
    public List<WishResponseDto> getWishList(Long id){
        String sql = "select w.id, p.name, p.image_url, w.quantity, p.price from Products p, WishList w, Members m "
                + "where w.memberid = m.id and w.productid = p.id and m.id = ?";
        List<WishResponseDto> mywishList = jdbcTemplate.query(sql, wishResponseDtoRowMapper(), id);
        return mywishList;
    }

    public void remove(Long wishListId){
        String sql = "delete from wishList where id = ?";
        jdbcTemplate.update(sql, wishListId);
    }

    private RowMapper<WishResponseDto> wishResponseDtoRowMapper(){
        return new RowMapper<WishResponseDto>() {
            @Override
            public WishResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long wishListId = rs.getLong("id");
                String productName = rs.getString("name");
                String image_url = rs.getString("image_url");
                Integer quantity = rs.getInt("quantity");
                Integer price = rs.getInt("price");
                return new WishResponseDto(wishListId, productName, image_url, quantity, price);
            }
        };
    }

}
