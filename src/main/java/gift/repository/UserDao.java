package gift.repository;

import gift.model.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDao {
    private JdbcClient jdbcClient;

    public UserDao(JdbcClient jdbcClient) {this.jdbcClient = jdbcClient;}

    public void createUser(User user) {
        jdbcClient.sql("INSERT INTO users (userid, password, role) VALUES (:userid, :password, :role)")
                .params(Map.of(
                        "userid", user.getUserid(),
                        "password", user.getPassword(),
                        "role", user.getRole()
                ))
                .update();
    }

    public Optional<User> findUserByUserid(String userid) {
        return jdbcClient.sql("SELECT * FROM users WHERE userid = :userid")
                .params(Map.of("userid", userid))
                .query(User.class)
                .optional();
    }

    public List<User> getAllUsers(){
        return jdbcClient.sql("SELECT * FROM users").query(User.class).list();
    }

    public void removeUser(Long id){
        jdbcClient.sql("DELETE FROM users WHERE id = :id")
                .params(Map.of("id",id))
                .update();
    }

    public Optional<User> findUserById(Long id) {
        return jdbcClient.sql("SELECT * FROM users WHERE id = :id")
                .params(Map.of("id", id))
                .query(User.class)
                .optional();
    }

    public void updateUser(Long id, User user) {
        jdbcClient.sql("""
        UPDATE users
        SET userid = :userid,
            password = :password,
            role = :role
        WHERE id = :id
    """)
                .params(Map.of(
                        "userid", user.getUserid(),
                        "password", user.getPassword(),
                        "role", user.getRole(),
                        "id", id
                ))
                .update();
    }
}