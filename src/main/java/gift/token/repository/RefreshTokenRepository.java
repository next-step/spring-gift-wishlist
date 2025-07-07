package gift.token.repository;

import gift.token.entity.RefreshToken;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

    private final JdbcClient jdbcClient;

    public RefreshTokenRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void save(RefreshToken refreshToken) {
        jdbcClient.sql(
                        "INSERT INTO refresh_token (token, member_uuid, created_at, expiration_date) "
                                + "VALUES (?, ?, ?, ?)")
                .param(refreshToken.getToken())
                .param(refreshToken.getMemberUuid())
                .param(refreshToken.getCreatedAt())
                .param(refreshToken.getExpirationDate())
                .update();
    }
}
