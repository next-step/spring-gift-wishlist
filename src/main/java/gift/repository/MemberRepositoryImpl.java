package gift.repository;

import gift.dto.Role;
import gift.entity.Member;
import gift.dto.MemberRequestDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
    public Member addMember(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("members").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("role", member.getRole().toString());
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getRole());
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

    @Override
    public List<Member> findAllMember() {
        String sql = "select * from members";
        return jdbcTemplate.query(sql, memberRowMapper());
    }

    @Override
    public Optional<Member> findMemberById(Long id) {
        String sql = "select * from members where id = ?";
        return jdbcTemplate.query(sql,memberRowMapper(), id).stream().findAny();
    }

    @Override
    public void modifyMember(Long id, Member member) {
        String sql = "update members set email = ?, password = ? where id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), id);
    }

    @Override
    public void removeMemberById(Long id) {
        String sql = "delete from members where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Member> memberRowMapper(){
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                return new Member(id, email, password, Role.valueOf(role));
            }
        };
    }
}


