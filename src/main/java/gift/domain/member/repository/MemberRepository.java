package gift.domain.member.repository;

import gift.domain.member.Member;
import gift.domain.product.Product;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";
        return jdbcClient.sql(sql)
            .param("email", email)
            .query(Member.class)
            .optional();
    }

    public Optional<Member> save(Member member) {
        String insertSql = "INSERT INTO member (email, password, name, role) VALUES (:email, :password, :name, :role)";
        jdbcClient.sql(insertSql)
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("name", member.getName())
                .param("role", member.getRole())
                .update();
        return Optional.of(member);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM member WHERE email = :email";
        Integer count = jdbcClient.sql(sql)
                .param("email", email)
                .query(Integer.class)
                .single();
        return count > 0;
    }

    public int update(Member member) {
        String updateSql = "UPDATE member SET email = :email, password = :password, name = :name, role = :role WHERE id = :id";

        return jdbcClient.sql(updateSql)
                .param("id", member.getId())
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("name", member.getName())
                .param("role", member.getRole())
                .update();
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query(Member.class)
                .optional();
    }

    public void delete(Long id){
        String sql = "DELETE FROM member WHERE id = :id";
        jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }

    public List<Member> getAll(){
        String sql = "SELECT * FROM member";
        return jdbcClient.sql(sql)
                .query(Member.class)
                .list();
    }
}
