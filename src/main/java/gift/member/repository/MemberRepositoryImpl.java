package gift.member.repository;

import gift.member.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveMember(Member member) {

        String sql = "INSERT INTO members(email, password, name) VALUES(?,?,?)";

        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getName());
    }

    @Override
    public Member findMemberByEmail(String email) {

        String sql = "SELECT memberId, email, password, name, role FROM members WHERE email = ?";

        return jdbcTemplate.queryForObject(sql,
            (rs, rowNum) -> new Member(rs.getLong("memberId"), rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("role")), email);
    }


}
