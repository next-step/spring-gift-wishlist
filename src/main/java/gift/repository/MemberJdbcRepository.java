package gift.repository;

import gift.domain.Member;
import gift.dto.CreateMemberRequest;
import gift.dto.UpdateMemberRequest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcClient client;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcRepository(DataSource dataSource) {
        this.client = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(CreateMemberRequest request) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(request);
        Number key = jdbcInsert.executeAndReturnKey(param);
        return new Member(key.longValue(), request.email(), request.password());
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select id, email, password from member where id = :id";
        return client.sql(sql)
                .param("id", id)
                .query(Member.class)
                .optional();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "select id, email, password from member where email = :email";
        return client.sql(sql)
                .param("email", email)
                .query(Member.class)
                .optional();
    }

    @Override
    public List<Member> findAll() {
        String sql = "select id, email, password from member";
        return client.sql(sql)
                .query(Member.class)
                .list();
    }

    @Override
    public Member update(Long id, UpdateMemberRequest request) {
        String sql = "update member set email = :newEmail, password = :password where id = :id";
        client.sql(sql)
                .param("newEmail", request.email())
                .param("password", request.password())
                .param("id", id)
                .update();
        return new Member(id, request.email(), request.password());
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from member where id = :id";
        client.sql(sql)
                .param("id", id)
                .update();
    }
}
