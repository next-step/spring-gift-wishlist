package gift.repository;

import org.springframework.jdbc.core.simple.JdbcClient;

public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

}
