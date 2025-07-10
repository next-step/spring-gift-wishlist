package gift.member.repository;

import gift.member.entity.Member;
import gift.exception.member.MemberNotFoundException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
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

        String sql = "INSERT INTO members(email, password, name, role) VALUES(?,?,?,?)";

        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getName(),
            member.getRole());
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

    @Override
    public List<Member> findAllMembers() {

        String sql = "SELECT memberId, email, password, name, role FROM members";

        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Member(rs.getLong("memberId"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("role")));
    }

    @Override
    public Member findMemberById(Long memberId) {

        String sql = "SELECT memberId, email, password, name, role FROM members WHERE memberId = ?";

        try {
            return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Member(rs.getLong("memberId"), rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("role")), memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다. memberId =" + memberId);
        }
    }

    @Override
    public void updateMember(Member member) {

        String sql = "UPDATE members SET email = ?, password = ?, name = ?, role = ? WHERE memberId = ?";

        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getName(),
            member.getRole(), member.getMemberId());
    }

    @Override
    public void deleteMember(Long memberId) {

        String sql = "DELETE FROM members WHERE memberId = ?";

        jdbcTemplate.update(sql, memberId);
    }
}
