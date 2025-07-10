package gift.wishlist.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistRepository {
    private final JdbcClient jdbcClient;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
