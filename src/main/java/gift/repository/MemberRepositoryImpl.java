package gift.repository;

import gift.entity.Member;
import gift.entity.RoleType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository{

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepositoryImpl(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("members")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Member> findAllMembers() {

        String sql = "select * from members";
        List<Member> result = jdbcClient.sql(sql)
                .query(Member.class)
                .list();

        return result;
    }

    @Override
    public Optional<Member> findMemberById(Long id) {

        String sql = "select * from members where id = ?";
        Optional<Member> result = jdbcClient.sql(sql)
                .param(id)
                .query(Member.class)
                .optional();

        return result;
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {

        String sql = "select * from members where email = ?";
        Optional<Member> result = jdbcClient.sql(sql)
                .param(email)
                .query(Member.class)
                .optional();

        return result;
    }

    @Override
    public Member saveMember(String email, String password, RoleType role) {

        final Map<String, Object> params = Map.of(
                "email", email,
                "password", password,
                "role", role
        );

        Number key = jdbcInsert.executeAndReturnKey(params);
        Long id = key.longValue();

        return new Member(id, email, password, role);
    }

    @Override
    public int updateMember(Long id, RoleType role) {

        String sql = """
                update members set role = :role
                where id = :id
                """;

        int rowNum = jdbcClient.sql(sql)
                .param("role", role.name())
                .param("id", id)
                .update();

        return rowNum;
    }

    @Override
    public int deleteMember(Long id) {
        String sql = "delete from members where id = ?";

        int rowNum = jdbcClient.sql(sql)
                .param(id)
                .update();

        return rowNum;
    }
}
