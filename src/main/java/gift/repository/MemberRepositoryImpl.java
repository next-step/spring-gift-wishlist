package gift.repository;

import gift.dto.Member;
import gift.dto.MemberRequestDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addMember(MemberRequestDto memberRequestDto) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("members").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("email", memberRequestDto.email());
        params.put("password", memberRequestDto.password());

        simpleJdbcInsert.execute(params);
    }

    @Override
    public Optional<Member> findMemberByEmailAndPassword(String email, String password) {
        String sql = "select * from members where email = ? and password = ?";
        return jdbcTemplate.query(sql, memberRowMapper(), email, password).stream().findAny();
    }


    @Override
    public Optional<Member> findMemberByEmail(String email) {
        String sql = "select * from members where email = ?";
        return jdbcTemplate.query(sql, memberRowMapper(), email).stream().findAny();
    }

    private RowMapper<Member> memberRowMapper(){
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                return new Member(id, email, password);
            }
        };
    }
}


