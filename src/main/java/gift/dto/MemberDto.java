package gift.dto;

import gift.Entity.Member;
import gift.Entity.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDto {

    private final JdbcClient client;

    public MemberDto(JdbcClient client) {
        this.client = client;
    }

    public void insertMember(Member member) {
        var sql = """
            INSERT INTO members (id, email, password, name, address, role)
            VALUES (:id, :email, :password, :name, :address, :role)
        """;
        client.sql(sql)
                .param("id", member.getId())
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("name", member.getName())
                .param("address", member.getAddress())
                .param("role", member.getRole())
                .update();
    }

    public Member selectEmail(String email) {
        var sql = """
            SELECT id, email, password, name, address, role
            FROM members
            WHERE email = :email
        """;
        return client.sql(sql)
                .param("email", email)
                .query(memberRowMapper())
                .optional()
                .orElse(null);
    }

    public Member selectId(String id) {
        var sql = """
            SELECT id, email, password, name, address, role
            FROM members
            WHERE id = :id
        """;
        return client.sql(sql)
                .param("id", id)
                .query(memberRowMapper())
                .optional()
                .orElse(null);
    }

    public void updateMember(String id, Member member) {
        var sql = """
        UPDATE members
        SET email = :email, password = :password, name = :name, address = :address, role = :role
        WHERE id = :id
    """;
        client.sql(sql)
                .param("id", id)
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("name", member.getName())
                .param("address", member.getAddress())
                .param("role", member.getRole())
                .update();
    }

    public void deleteMember(String id) {
        var sql = """
                DELETE FROM members WHERE id = :id
        """;
        client.sql(sql)
                .param("id", id)
                .update();
    }

    public List<Member> showallMembers() {
        var sql = """
            SELECT id, email, password, name, address, role
            FROM members
        """;
        return client.sql(sql)
                .query(memberRowMapper())
                .list();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(
                rs.getString("id"),     // ← 여기 수정됨
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("role")
        );
    }
}
