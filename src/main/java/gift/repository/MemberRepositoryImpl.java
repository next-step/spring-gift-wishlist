package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    // JdbcClient
    private final JdbcClient client;

    public MemberRepositoryImpl(JdbcClient client) {
        this.client = client;
    }

    @Override
    public Long saveMember(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        var sql = "INSERT INTO member (email, password, role) VALUES (:email, :password, :role)";
        client.sql(sql)
              .param("email", member.getEmail())
              .param("password", member.getPassword())
              .param("role", member.getRole().name())
              .update(keyHolder);

        return keyHolder.getKey().longValue();
    }
}
