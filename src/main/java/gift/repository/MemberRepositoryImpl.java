package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.exception.MemberNotFoundException;
import gift.exception.ProductNotFoundException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public int addMember(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbcClient.sql("insert into member (email, password, name, role) values (:email, :password, :name, :role)")
                .param("email", member.email())
                .param("password", member.password())
                .param("name", member.name())
                .param("role", member.role().toUpperCase())
                .update();
        return result;
    }

    @Override
    public Member findMemberByIdOrElseThrow(Long id) {
        Optional<Member> member = jdbcClient.sql("select id, email, password, name, role from member where id = :id")
                .param("id", id)
                .query(Member.class)
                .optional();
        return member.orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public Member findMemberByEmail(String email) {
        Optional<Member> member = jdbcClient.sql("select id, email, password, name, role from member where email = :email")
                .param("email", email)
                .query(Member.class)
                .optional();
        return member.orElse(null);
    }

    @Override
    public List<Member> findAllMembers() {
        List<Member> members = jdbcClient.sql("select id, email, password, name, role from member")
                .query(Member.class)
                .list();
        return members;
    }

    @Override
    public int updateMemberById(Member member) {
        int result = jdbcClient.sql("update member set name = :name, role = :role, email = :email where id = :id")
                .param("name", member.name())
                .param("role", member.role().toUpperCase())
                .param("email", member.email())
                .param("id", member.id())
                .update();
        return result;
    }

    @Override
    public int deleteMemberById(Long id) {
        int result = jdbcClient.sql("delete from member where id = :id")
                .param("id", id)
                .update();
        return result;
    }

}
