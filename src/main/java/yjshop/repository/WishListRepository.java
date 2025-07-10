package yjshop.repository;

import yjshop.dto.wish.WishResponseDto;
import yjshop.entity.Product;
import yjshop.entity.WishList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public boolean checkExistence(Long memberId, Integer quantity, Product product){
        String sql = "select id from wishlist where memberid = ? and productid = ?";
        try{
            Long wishListId = jdbcTemplate.queryForObject(sql, Long.class, memberId, product.getId());
            updateQuantity(memberId, wishListId, quantity);
        }
        catch (EmptyResultDataAccessException e){
            return false;
        }
        return true;
    }

    //TODO: WishList에 대한 CRUD
    //조회
    public List<WishResponseDto> getWishList(Long id){
        String sql = "select w.id, p.name, p.image_url, w.quantity, p.price from Products p, WishList w, Members m "
                + "where w.memberid = m.id and w.productid = p.id and m.id = ?";
        List<WishResponseDto> mywishList = jdbcTemplate.query(sql, wishResponseDtoRowMapper(), id);
        return mywishList;
    }

    public WishResponseDto getWishProduct(Long memberId, Long productId){
        String sql = "select w.id, p.name, p.image_url, w.quantity, p.price from Products p, WishList w, Members m "
                + "where w.memberid = m.id and w.productid = p.id and m.id = ? and p.id = ? ";
        List<WishResponseDto> mywishList = jdbcTemplate.query(sql, wishResponseDtoRowMapper(), memberId, productId);
        return mywishList.get(0);
    }

    public Long getProductIdByWithId(Long wishListId){
        String sql = "select productid from whishlist where id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, wishListId);
    }

    public void remove(Long wishListId){
        String sql = "delete from wishList where id = ?";
        jdbcTemplate.update(sql, wishListId);
    }

    public void updateQuantity(Long memberId, Long wishListId, int amount){
        String sql = "select quantity from wishlist where memberid = ? and id = ?";
        Integer quantity = jdbcTemplate.queryForObject(sql, Integer.class, memberId, wishListId);

        Integer updateQuantity = quantity + amount;

        if(updateQuantity == 0){
            remove(wishListId);
            return;
        }

        String udpateSql = "update wishlist set quantity = ? where memberid = ? and id = ?";
        jdbcTemplate.update(udpateSql, updateQuantity, memberId, wishListId);
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
                return new WishResponseDto(wishListId, productName, image_url, quantity, price * quantity);
            }
        };
    }

}
