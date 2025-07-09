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
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    public WishItem addWishItem(String name, Integer quantity, String userEmail) {

        User user = userRepository.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException(userEmail);
        }

        Item item = jdbcTemplate.queryForObject(
                "SELECT id, name, price, image_url FROM items WHERE name = ? LIMIT 1", new Object[]{name}, itemRowMapper
        );

        if (item == null) {
            throw new ItemNotFoundException(name);
        }

        String insertSql = "INSERT INTO wish_items (user_id, item_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, user.id(), item.getId(), quantity);

        return new WishItem(item.getId(), item.getName(), item.getImageUrl(), item.getPrice(), quantity);
    }
}
