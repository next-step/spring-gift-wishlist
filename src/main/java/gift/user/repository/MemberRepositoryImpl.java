package gift.user.repository;

import gift.user.entity.Member;
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
}
