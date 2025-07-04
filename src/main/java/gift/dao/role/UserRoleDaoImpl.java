package gift.dao.role;

import gift.entity.UserRole;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {
    private final JdbcClient jdbcClient;


    public UserRoleDaoImpl(JdbcClient client) {
        this.jdbcClient = client;
    }

    private static class UserRoleRowMapper implements RowMapper<UserRole>{
        @Override
        public UserRole mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            String roleName = rs.getString("role_name");
            return UserRole.fromString(roleName);
        }
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        String sql = """
                SELECT role_name FROM user_roles WHERE user_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(userId)
                .query(new UserRoleRowMapper())
                .stream()
                .toList();
    }

    @Override
    public Boolean exists(Long userId, UserRole role) {
        String sql = """
                SELECT COUNT(*) FROM user_roles WHERE user_id = ? AND role_name = ?
                """;
        return jdbcClient.sql(sql)
                .param(userId)
                .param(role.toString())
                .query(Integer.class)
                .single() > 0;
    }

    @Override
    public Integer save(Long userId, UserRole role) {
        String sql = """
                INSERT INTO user_roles (user_id, role_name)
                VALUES (?, ?)
                """;
        return jdbcClient.sql(sql)
                .param(userId)
                .param(role.toString())
                .update();
    }

    @Override
    public Integer delete(Long userId, UserRole role) {
        String sql = """
                DELETE FROM user_roles WHERE user_id = ? AND role_name = ?
                """;
        return jdbcClient.sql(sql)
                .param(userId)
                .param(role.toString())
                .update();
    }
}
