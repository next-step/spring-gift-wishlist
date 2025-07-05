package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Long> createMember(Member member) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into member (email, password) values (:email, :password)")
            .param("email", member.getEmail())
            .param("password", member.getPassword())
            .update(keyHolder);
        return Optional.ofNullable(keyHolder.getKeyAs(Long.class));
    }

    public Boolean checkEmailExists(String email) {
        return jdbcClient.sql("select count(*) from member where email = :email")
            .param("email", email)
            .query(Long.class)
            .single() == 1;
    }
}
