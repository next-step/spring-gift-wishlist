package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao implements MemberRepository{
    private final JdbcClient client;
    public MemberDao(JdbcClient client){
        this.client = client;
    }
    private final RowMapper<Member> getMemberRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");

        return new Member(id, email, password, role);
    };

    @Override
    public Member createMember(Member newMember) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into members(email, password, role) values (:email, :password, :role);";
        client.sql(sql)
                .param("email", newMember.getEmail())
                .param("password", newMember.getPassword())
                .param("role", newMember.getRole())
                .update(keyHolder);

        Member savedMember = new Member(keyHolder.getKey().longValue(), newMember.getEmail(),
                newMember.getPassword(), newMember.getRole());

        return savedMember;
    }

    @Override
    public Member loginMember(Member member) {
        return null;
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        String sql = "select id, email, password, role from members where email = :email;";
        return client.sql(sql)
                .param("email", email)
                .query(getMemberRowMapper)
                .optional();
    }
}
