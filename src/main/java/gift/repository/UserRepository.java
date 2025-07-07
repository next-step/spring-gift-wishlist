package gift.repository;

import gift.entity.User;
import gift.exception.gift.NoValueException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static gift.status.UserErrorStatus.*;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers(){
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    public Optional<User> findUserByEmail(String email){
        String sql = "SELECT * FROM USERS WHERE EMAIL = ?;";
        Object[] args = new Object[]{email};
        int[] argTypes = {Types.VARCHAR};
        return jdbcTemplate.query(sql, args, argTypes, rs -> {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setEmail(rs.getString("EMAIL"));
                        user.setPassword(rs.getString("PASSWORD"));
                        return Optional.of(user);
                    }
                    return Optional.empty();
                }
        );
    }

    public void deleteById(Long userId){
        String sql = "DELETE FROM USERS WHERE ID = ?;";
        Object[] args = new Object[]{userId};
        jdbcTemplate.update(sql, args);
    }

    public void modify(Long userId, User user){
        StringBuilder sql = new StringBuilder("UPDATE USERS SET ");
        List<Object> params = new ArrayList<>();
        if(user.getEmail() != null){
            sql.append("email = ?, ");
            params.add(user.getEmail());
            System.out.println("user.getEmail() = " + user.getEmail());
        }
        if(user.getPassword() != null){
            sql.append("password = ?, ");
            params.add(user.getPassword());
            System.out.println("user.getPassword() = " + user.getPassword());
        }
        if(params.isEmpty()){
            throw new NoValueException(NO_VALUE.getErrorMessage());
        }
        sql.deleteCharAt((sql.length()-2));
        sql.append("WHERE id = ?");
        params.add(userId);
        jdbcTemplate.update(sql.toString(), params.toArray());
    }
}
