package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.dto.api.product.ProductResponseDto;
import gift.entity.Member;
import gift.entity.Product;
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
}
