package gift.member.repository;

import gift.member.domain.Member;
import gift.member.domain.RoleType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient client;

    public MemberRepository(JdbcClient client) {
        this.client = client;
    }

    public Member save(String email, String password, RoleType role){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        client.sql("insert into member (email, password, role) values (:email, :password, :role)")
                .param("email", email)
                .param("password", password)
                .param("role", role.toString())
                .update(keyHolder);

        return new Member(keyHolder.getKey().longValue(), email, password, role);
    }

    public Optional<Member> findByEmail(String email) {
        return client.sql("select id, email, password, role from member where email = :email")
                .param("email", email)
                .query(memberRowMapper)
                .optional();
    }

    public boolean updatePassword(Long id, String newPassword) {
        int affected = client.sql("update member set password = :password where id = :id")
                .param("password", newPassword)
                .param("id", id)
                .update();

        return affected > 0;
    }

    public boolean deleteById(Long id) {
        int affected = client.sql("delete from member where id = :id")
                .param("id", id)
                .update();

        return affected > 0;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            RoleType.valueOf(rs.getString("role"))
    );
}
