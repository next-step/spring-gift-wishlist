package gift.repository.wishListRepository;

import gift.entity.Item;
import gift.entity.User;
import gift.entity.WishItem;
import gift.exception.itemException.ItemNotFoundException;
import gift.exception.userException.UserNotFoundException;
import gift.repository.itemRepository.ItemRepository;
import gift.repository.userRepository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WishListRepositoryImpl implements WishListRepository {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final JdbcTemplate jdbcTemplate;

    public WishListRepositoryImpl(UserRepository userRepository, ItemRepository itemRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Item> itemRowMapper = new RowMapper<>() {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Item(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
            );
        }
    };

    @Override
    public WishItem addWishItem(Long itemId, String itemName, String imageUrl, Integer price, Integer quantity, Long userId) {
        var insertSql = "INSERT INTO wish_items (user_id, item_id, quantity) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setLong(2, itemId);
            ps.setInt(3, quantity);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new WishItem(id, itemName, imageUrl, price, quantity);
    }

    @Override
    public List<WishItem> getAllWishItems(String userEmail) {
        String sql = """
        SELECT
            i.id AS item_id,
            i.name,
            i.image_url,
            i.price,
            w.quantity
        FROM wish_items w
        JOIN users u ON w.user_id = u.id
        JOIN items i ON w.item_id = i.id
        WHERE u.email = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{userEmail}, (rs, rowNum) -> new WishItem(
                rs.getLong("item_id"),
                rs.getString("name"),
                rs.getString("image_url"),
                rs.getInt("price"),
                rs.getInt("quantity")
        ));
    }

    @Override
    public List<WishItem> getWishItems(String name, Integer price, String userEmail) {
        StringBuilder sql = new StringBuilder("""
        SELECT
            i.id AS item_id,
            i.name,
            i.image_url,
            i.price,
            w.quantity
        FROM wish_items w
        JOIN users u ON w.user_id = u.id
        JOIN items i ON w.item_id = i.id
        WHERE u.email = ?
    """);

        if (name != null) {
            sql.append(" AND i.name = ?");
        }
        if (price != null) {
            sql.append(" AND i.price = ?");
        }

        var params = new java.util.ArrayList<>();
        params.add(userEmail);
        if (name != null) params.add(name);
        if (price != null) params.add(price);

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) ->
                new WishItem(
                        rs.getLong("item_id"),
                        rs.getString("name"),
                        rs.getString("image_url"),
                        rs.getInt("price"),
                        rs.getInt("quantity")
                )
        );
    }

    @Override
    public WishItem deleteItem(String name, String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        Item item = jdbcTemplate.queryForObject(
                "SELECT id, name, price, image_url FROM items WHERE name =?",
                new Object[]{name},
                (rs, rowNum) -> new Item(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("image_url")
                )
        );

        if (item == null) {
            throw new ItemNotFoundException(name);
        }

        var quantitySql = "SELECT w.quantity FROM wish_items w WHERE w.user_id=? AND w.item_id=?";
        Integer quantity = jdbcTemplate.queryForObject(quantitySql, new Object[]{user.id(), item.getId()}, Integer.class);

        var sql = "DELETE FROM wish_items WHERE user_id=? AND item_id=?";
        jdbcTemplate.update(sql, user.id(), item.getId());

        return new WishItem(item.getId(), item.getName(), item.getImageUrl(), item.getPrice(),quantity);
    }

    @Override
    public WishItem updateWishItem(Integer quantity, String name, String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        Item item = jdbcTemplate.queryForObject(
                "SELECT id, name, price, image_url FROM items WHERE name =? LIMIT 1",
                new Object[]{name},
                (rs,rowNum) -> new Item(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("image_url")
                )
        );

        if (item == null) {
            throw new ItemNotFoundException(name);
        }

        var wishListSql = "SELECT COUNT(*) FROM wish_items WHERE user_id =? AND item_id=?";
        Integer count = jdbcTemplate.queryForObject(wishListSql, new Object[]{user.id(), item.getId()}, Integer.class);

        if (count == null || count == 0) {
            throw new ItemNotFoundException("해당 상품은 위시 리스트에 없습니다: " + name);
        }

        var updateSql = "UPDATE wish_items SET quantity=? WHERE user_id=? AND item_id=?";
        jdbcTemplate.update(updateSql, quantity, user.id(), item.getId());

        return new WishItem(item.getId(), item.getName(), item.getImageUrl(), item.getPrice(), quantity);
    }


}
