package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    private static final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
        rs.getLong("identify_number"),
        rs.getString("email"),
        rs.getString("password"),
        rs.getString("authority")
    );

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Long> createMember(Member member) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into member (email, password, authority) values (:email, :password, :authority)")
            .param("email", member.getEmail())
            .param("password", member.getPassword())
            .param("authority", member.getAuthorities().stream().findFirst().get().getAuthority())
            .update(keyHolder);
        return Optional.ofNullable(keyHolder.getKeyAs(Long.class));
    }

    public Boolean checkEmailExists(String email) {
        return jdbcClient.sql("select count(*) from member where email = :email")
            .param("email", email)
            .query(Long.class)
            .single() == 1;
    }

    public Optional<Member> findByEmail(String email) {
        return jdbcClient.sql("select * from member where email = :email")
            .param("email", email)
            .query(memberRowMapper)
            .optional();
    }

}
