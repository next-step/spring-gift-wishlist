package gift.repository;

import gift.domain.WishList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class WishListJdbcRepository implements WishListRepository {
    private final JdbcTemplate jdbcTemplate;

    public WishListJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(WishList wishList) {
        
    }
}
