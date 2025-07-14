package gift.repository;

import gift.domain.Member;
import gift.domain.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepository {
    private final JdbcClient jdbc;

    public WishRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public void save(String memberId, Long productId) {
        jdbc.sql("INSERT INTO wish (member_id, product_id) VALUES (:memberId, :productId)")
                .param("memberId", memberId)
                .param("productId", productId)
                .update();
    }

    public List<Wish> findByMemberId(String memberId) {
        return jdbc.sql("SELECT * FROM wish WHERE member_id = :memberId")
                .param("memberId", memberId)
                .query(Wish.class)
                .list();
    }

    public void delete(String memberId, Long productId) {
        jdbc.sql("DELETE FROM wish WHERE member_id = :memberId AND product_id = :productId")
                .param("memberId", memberId)
                .param("productId", productId)
                .update();
    }
}
