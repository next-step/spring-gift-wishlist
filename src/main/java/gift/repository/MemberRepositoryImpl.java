package gift.repository;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    @Autowired
    public MemberRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public int create(MemberRequestDto requestDto) {
        String sql = "INSERT INTO member VALUES(:email, :password)";

        int createRow = jdbcClient.sql(sql)
            .param("email", requestDto.getEmail())
            .param("password", requestDto.getPassword())
            .update();

        return createRow;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";

        return jdbcClient.sql(sql)
            .param("email", email)
            .query(Member.class)
            .optional();
    }


}
