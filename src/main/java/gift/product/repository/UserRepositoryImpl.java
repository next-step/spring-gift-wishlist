package gift.product.repository;


import gift.product.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;


@Repository
public class UserRepositoryImpl implements UserRepository {

	private final JdbcTemplate jdbcTemplate;

	public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public Long save(User user) {
		final String sql = "INSERT INTO users (email, password, nickname) VALUES (?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getNickName());
			return ps;
		}, keyHolder);

		return keyHolder.getKey().longValue();
	}


	@Override
	public Optional<User> findById(Long id) {
		final String sql = "SELECT * FROM users WHERE id = ?";

		try {
			User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
				new User(
					rs.getLong("id"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getString("nickname")
				), id
			);
			return Optional.of(user);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<User> findByEmail(String email) {
		final String sql = "SELECT * FROM users WHERE email = ?";

		try {
			User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
				new User(
					rs.getLong("id"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getString("nickname")
				), email
			);
			return Optional.of(user);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<User> findByNickname(String nickName) {
		final String sql = "SELECT * FROM users WHERE nickname = ?";

		try {
			User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
				new User(
					rs.getLong("id"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getString("nickname")
				), nickName
			);
			return Optional.of(user);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
