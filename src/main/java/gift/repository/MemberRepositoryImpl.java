package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepositoryImpl(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Member register(Member member){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("insert into members (email, pwd) values (:email, :pwd)")
                .param("email", member.getEmail())
                .param("pwd", member.getPwd())
                .update(keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Member(id, member.getEmail(), member.getPwd());
    }

    @Override
    public Optional<Member> findByEmail(String email){
        return jdbcClient.sql("select * from members where email = :email")
                .param("email", email)
                .query(Member.class)
                .optional();
    }

}
