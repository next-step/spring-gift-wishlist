package gift.repository.member;

import gift.entity.Member;
import gift.entity.Role;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    
    private final JdbcClient members;
    
    public MemberRepositoryImpl(JdbcClient members) {
        this.members = members;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT EXISTS (SELECT 1 FROM members WHERE email = :email)";
        
        Boolean exists = members.sql(sql)
            .param("email", email)
            .query(Boolean.class)
            .single();
        
        return Boolean.TRUE.equals(exists);
    }
    
    @Override
    public Member registerMember(Member newMember) {
        var sql = """
            insert into members(email, password, role)
            values (:email, :password, :role);
            """;
        GeneratedKeyHolder generatedKey = new GeneratedKeyHolder();
        
        members.sql(sql)
            .param("email", newMember.getEmail())
            .param("password", newMember.getPassword())
            .param("role", newMember.getRole().name())
            .update(generatedKey);
        
        long recentKey = generatedKey.getKey().longValue();
        
        return new Member(recentKey, newMember.getEmail(), newMember.getPassword(), newMember.getRole());
    }
    
    @Override
    public Member findMemberByEmail(String email) {
        var sql = """
            select * from members where email = :email;
            """;
        
        return members.sql(sql)
            .param("email", email)
            .query((rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
            )).optional()
            .orElseThrow(WrongIdOrPasswordException::new);
    }
    
    @Override
    public Member findMemberById(Long userId) {
        var sql = """
            select * from members where id = :id;
            """;
        
        return members.sql(sql)
            .param("id", userId)
            .query((rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
            )).optional()
            .orElseThrow(WrongIdOrPasswordException::new);
    }
}
