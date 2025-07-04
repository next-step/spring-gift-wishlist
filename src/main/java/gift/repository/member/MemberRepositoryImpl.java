package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.dto.api.product.ProductResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
    public boolean alreadyRegistered(String email) {
        var sql = """
            select count(*) from members where email = :email;
            """;
        
        Integer memberCnt =  members.sql(sql)
            .param("email", email)
            .query(Integer.class)
            .single();
        
        return memberCnt > 0;
    }
    
    @Override
    public MemberResponseDto registerMember(Member newMember) {
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
        
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String accessToken = Jwts.builder()
            .subject(Long.toString(recentKey))
            .claim("email", newMember.getEmail())
            .claim("role", newMember.getRole())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
        
        return new MemberResponseDto(accessToken);
    }
    
    @Override
    public boolean wrongPassword(String email, String password) {
        var sql = """
            select password from members where email = :email;
            """;
        
        String registeredPassword =  members.sql(sql)
            .param("email", email)
            .query(String.class)
            .single();
        
        return !registeredPassword.equals(password);
    }
    
    @Override
    public Member findMember(String email, String password) {
        var sql = """
            select * from members where email = :email and password = :password;
            """;
        
        return members.sql(sql)
            .param("email", email)
            .param("password", password)
            .query((rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
            )).single();
    }
    
    @Override
    public MemberResponseDto loginMember(Member member) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String accessToken = Jwts.builder()
            .subject(Long.toString(member.getId()))
            .claim("email", member.getEmail())
            .claim("role", member.getRole())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
        
        return new MemberResponseDto(accessToken);
    }
}
