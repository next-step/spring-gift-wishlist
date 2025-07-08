package gift.user.dao;

import gift.user.domain.Role;
import gift.user.domain.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

  private final JdbcClient jdbcClient;

  public UserDao(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public User saveUser(String email, String encodedPassword) {
    String sql = "insert into users (email,password) values (?, ?)";

    jdbcClient.sql(sql)
        .param(email)
        .param(encodedPassword)
        .update();

    String sqlForFind = "SELECT memberId, email, password, role FROM users"
        + " WHERE email = ?";
    return jdbcClient.sql(sqlForFind)
        .param(email)
        .query((result, rowNum) -> {
          User user = new User(
              result.getLong("memberId"),
              result.getString("email"),
              result.getString("password"),
              Role.valueOf(result.getString("role"))
          );
          return user;
        })
        .single();
  }

  public User findByEmail(String email) {
    String sql = "select memberId, email, password, role from users where email = ?";

    return jdbcClient.sql(sql)
        .param(email)
        .query((result, rowNum) -> {
          User user = new User(
              result.getLong("memberId"),
              result.getString("email"),
              result.getString("password"),
              Role.valueOf(result.getString("role"))
          );
          return user;
        })
        .single();
  }
}
