package gift.repository;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void saveMember(String email, String password, String role) {
        String sql = "insert into members(email, password, role) values(?,?,?)";
        jdbcTemplate.update(sql, email, password, role);
    }

    public Integer countMember(String email, String password) {
        var sql = "select count(*) from members where email = ? and password = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class ,email, password);
    }
}
