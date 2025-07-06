package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("MemberRepository")
public class MemberRepository implements MemberRepositoryInterface {
    private JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member findByEmail(String email) {
        return null;
    }

    @Override
    public void save(Member member) {

    }
}
