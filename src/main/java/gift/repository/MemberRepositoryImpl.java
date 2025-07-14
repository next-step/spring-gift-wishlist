package gift.repository;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Override
    public Member findByEmail(String email) {
        var sql = "select id,email,password,role from members where email = ?";
        try{
            Member member = jdbcTemplate.queryForObject(
                sql,
                new Object[]{email},  // ← 파라미터 추가
                (resultSet, rowNum) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                ));
            return member;

        } catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    @Override
    public void deleteAllMembers() {
        String sql = "delete from members";
        jdbcTemplate.update(sql);
    }
}
