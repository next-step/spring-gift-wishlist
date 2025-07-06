package gift.repository.member;

import gift.entity.Member;
import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class MemberJdbcRepositoryImpl implements MemberRepository {

  private JdbcTemplate jdbcTemplate;

  public MemberJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    String sql = "select * from members where email=?";
    try {
      Member result = jdbcTemplate.queryForObject(sql,
          (rs, rowNum) -> new Member(
              rs.getLong("id"),
              rs.getString("email"),
              rs.getString("password")
          )
          , email);
      return Optional.of(result);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Member createMember(Member member) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String sql = "insert into members (email,password) values (?,?)";
    jdbcTemplate.update((connection) -> {
      PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
      ps.setString(1, member.getEmail());
      ps.setString(2, member.getPassword());
      return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key == null) {
      throw new IllegalStateException("생성된 ID가 존재하지 않습니다.");
    }
    Long generatedId = key.longValue();
    member.setId(generatedId);
    return member;
  }

}
