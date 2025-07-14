package gift.product.repository;


import gift.product.entity.WishList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


@Repository
public class WishListRepositoryImpl implements WishListRepository {

	private final JdbcTemplate jdbcTemplate;
	public WishListRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<WishList> findAll(Long userId) {
		final String sql = "SELECT * FROM wish_list where user_id = ?";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new WishList(
			rs.getLong("id"),
			rs.getLong("user_id"),
			rs.getLong("item_id"),
			rs.getInt("amount")
		), userId);
	}

	@Override
	public Long save(WishList wishList) {
		final String sql = "INSERT INTO wish_list (user_id, item_id) VALUES (?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, wishList.getUserId());
			ps.setLong(2, wishList.getItemId());
			return ps;
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public void delete(Long userId, Long itemId) {
		final String sql = "DELETE FROM wish_list WHERE user_id = ? AND item_id = ?";
		jdbcTemplate.update(sql, userId, itemId);
	}

	public Optional<WishList> findByUserIdAndItemId(Long userId, Long itemId) {
		final String sql = "SELECT * FROM wish_list WHERE user_id = ? AND item_id = ?";
		List<WishList> result = jdbcTemplate.query(sql, (rs, rowNum) -> new WishList(
			rs.getLong("id"),
			rs.getLong("user_id"),
			rs.getLong("item_id"),
			rs.getInt("amount")
		), userId, itemId);

		if(result.isEmpty()) return Optional.empty();
		else return Optional.of(result.get(0));
	}


	@Override
	public void decreaseAmount(Long userId, Long itemId) {
		final String sql = "UPDATE wish_list SET amount = amount - 1 WHERE user_id = ? AND item_id = ?";
		jdbcTemplate.update(sql, userId, itemId);
	}


	@Override
	public void increaseAmount(Long userId, Long itemId) {
		final String sql = "UPDATE wish_list SET amount = amount + 1 WHERE user_id = ? AND item_id = ?";
		jdbcTemplate.update(sql, userId, itemId);
	}

}
