package gift.member.repository;

import gift.member.entity.Member;
import gift.member.entity.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final RowMapper<Member> getMemberRowMapper =
            (rs, rowNum) ->
                    new Member(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role"))
                    );

    @Override
    public Member saveMember(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO member(name, email, password, role) VALUES (:name, :email, :password, :role)")
                .param("name", member.getName())
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("role", member.getRole().name())
                .update(keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        return jdbcClient.sql("SELECT * FROM member WHERE email = :email")
                .param("email", email)
                .query(getMemberRowMapper)
                .optional();
    }

    @Override
    public Optional<Member> findMemberById(Long id) {
        return jdbcClient.sql("SELECT * FROM member WHERE id = :id")
                .param("id", id)
                .query(getMemberRowMapper)
                .optional();
    }

    @Override
    public List<Member> findAllMembers() {
        return jdbcClient.sql("SELECT * FROM member")
                .query(getMemberRowMapper)
                .list();
    }

    @Override
    public void updateMember(Member member) {
        jdbcClient.sql("UPDATE member SET email = :email, password = :password WHERE id = :id")
                .param("name", member.getName())
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("id", member.getId())
                .update();
    }

    @Override
    public void deleteMember(Long id) {
        jdbcClient.sql("DELETE FROM member WHERE id = :id")
                .param("id", id)
                .update();
    }
}
