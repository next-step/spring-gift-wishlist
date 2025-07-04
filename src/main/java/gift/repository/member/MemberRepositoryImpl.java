package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.dto.api.product.ProductResponseDto;
import gift.entity.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
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
        return null;
    }
}
