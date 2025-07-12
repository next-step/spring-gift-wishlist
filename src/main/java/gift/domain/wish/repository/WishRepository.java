package gift.domain.wish.repository;

import gift.domain.wish.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishRepository {
    private JdbcClient jdbcClient;

    public WishRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public int save(Wish wish){
        String insertSql = "INSERT INTO wish (member_id, product_id, quantity) VALUES (:member_id, :product_id, :quantity)";
        return jdbcClient.sql(insertSql)
                .param("member_id", wish.getMemberId())
                .param("product_id", wish.getProductId())
                .param("quantity", wish.getQuantity())
                .update();
    }

    public Optional<Wish> get(Long id, Long memberId) {
        String sql = "SELECT * FROM wish WHERE id = :id AND member_id = :memberId";
        return jdbcClient.sql(sql)
                .param("id", id)
                .param("memberId", memberId)
                .query(Wish.class)
                .optional();
    }

    public int delete(Long id, Long memberId){
        String sql = "DELETE FROM wish WHERE id = :id AND member_id = :memberId";
        return jdbcClient.sql(sql)
                .param("id", id)
                .param("memberId", memberId)
                .update();
    }

    public List<Wish> getAll(Long memberId){
        String sql = "SELECT * FROM wish WHERE member_id = :memberId";
        return jdbcClient.sql(sql)
                .param("memberId", memberId)
                .query(Wish.class)
                .list();
    }

    public int update(Wish wish){
        String updateSql = "UPDATE wish SET quantity = :quantity WHERE id = :id AND member_id = :memberId";

        return jdbcClient.sql(updateSql)
                .param("id", wish.getId())
                .param("quantity", wish.getQuantity())
                .param("memberId", wish.getMemberId())
                .update();
    }
}
