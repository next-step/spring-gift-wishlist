package gift.repository.member;

import gift.domain.Member;
import gift.dto.member.MemberRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper = (rs, rowNum) -> new Member(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password")
    );
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findById(Long memberId){
        String sql = "select * from members where id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper, memberId);
    }

    public Member findByEmail(String email){
        String sql = "select * from members where email = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    public Long insert(Member member){
        String sql = "insert into members(email, password) "
            + "values(?,?,) ";

        jdbcTemplate.update((Connection con) -> {
            PreparedStatement st = con.prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, member.getEmail());
            st.setString(2, member.getPassword());
            return st;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void update(Long memberId, MemberRequest request){
        String sql = "update members set email = ?, password = ? where id = ?";
        jdbcTemplate.update(sql, request.email(), request.password(), memberId);
    }

    public void deleteById(Long memberId){
        String sql = "delete from members where id = ? ";
        jdbcTemplate.update(sql, memberId);
    }

}
