package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // 회원 저장
    public Member save(Member member) {
        String sql = "INSERT INTO members (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
            .sql(sql)
            .param(member.getEmail())
            .param(member.getPassword())
            .update(keyHolder, "id");

        Long id = keyHolder.getKey().longValue();
        return new Member(id, member.getEmail(), member.getPassword());
    }

    public boolean existByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM members WHERE email = ?";
        Integer count = jdbcClient
            .sql(sql)
            .param(email)
            .query(Integer.class)
            .optional()
            .orElse(0);

        return count > 0;
    }

}
