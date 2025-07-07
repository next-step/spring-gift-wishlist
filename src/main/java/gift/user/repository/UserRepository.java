package gift.user.repository;

import gift.product.entity.Product;
import gift.user.entity.Role;
import gift.user.entity.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private final JdbcClient jdbcClient;

  public UserRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public User saveUser(String email, String password) {
    String sql = "insert into users (email,password) values (?, ?)";

    jdbcClient.sql(sql)
        .param(email)
        .param(password)
        .update();

    String sqlForFind = "SELECT memberId, email, password, role FROM users"
        + " WHERE email = ? AND password = ?";
    return jdbcClient.sql(sqlForFind)
        .param(email)
        .param(password)
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
