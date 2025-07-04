package gift.member.repository;

import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
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

        String sql = "INSERT INTO members(email, password) VALUES(?,?)";

        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    @Override
    public void findMemberByEmail(String email) {

        String sql = "SELECT email, password FROM members WHERE email = ?";

        try {
            jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Member(rs.getString("email"), rs.getString("password")), email);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException("가입된 이메일이 존재하지 않습니다.");
        }
    }


}
