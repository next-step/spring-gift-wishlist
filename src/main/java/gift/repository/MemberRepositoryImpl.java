package gift.repository;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    private final static RowMapper<Member> MEMBER_ROW_MAPPER = ((rs, rowNum) -> new Member(
        rs.getString("email"),
        rs.getString("password")
    ));

    @Autowired
    public MemberRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public int create(MemberRequestDto requestDto) {
        String sql = "INSERT INTO member VALUES(:email, :password)";

        int createRow = jdbcClient.sql(sql)
            .param("email", requestDto.email())
            .param("password", requestDto.password())
            .update();

        return createRow;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";

        return jdbcClient.sql(sql)
            .param("email", email)
            .query(MEMBER_ROW_MAPPER)
            .optional();
    }

    @Override
    public void changePassword(Member member) {
        String sql = "UPDATE member SET password = :password WHERE email = :email";

        jdbcClient.sql(sql)
            .param("password", member.getPassword())
            .param("email", member.getEmail())
            .update();
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT count(*) FROM member WHERE email = :email";

        int findCount = jdbcClient.sql(sql)
            .param("email", email)
            .query(Integer.class)
            .single();

        return findCount > 0;
    }
}
