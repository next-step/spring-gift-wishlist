package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepositoryImpl(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void register(Member member){
        jdbcClient.sql("insert into members (email, password) values (:email, :password)")
                .params("email", member.email())
                .param("password", member.password())
                .update();
    }

    @Override
    public Optional<Member> findByEmail(String email){
        return jdbcClient.sql("select * from members where email = :email")
                .param("email", email)
                .query(Member.class)
                .optional();
    }

}
