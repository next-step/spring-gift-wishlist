package gift.repository;

import gift.domain.Member;
import gift.dto.CreateMemberRequest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcClient client;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcRepository(DataSource dataSource) {
        this.client = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member");
    }

    @Override
    public Member save(CreateMemberRequest request) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(request);
        jdbcInsert.execute(param);
        return new Member(request.email(), request.password());
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "select email, password from member where email = :email";
        return client.sql(sql)
                .param("email", email)
                .query(Member.class)
                .optional();
    }
}
