package gift.repository;

import gift.dto.Member;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addMember(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("members").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());

        simpleJdbcInsert.execute(params);
    }

    @Override
    public void findMemberByEmailAndPassword() {

    }

    @Override
    public void findMemberById() {

    }
}
