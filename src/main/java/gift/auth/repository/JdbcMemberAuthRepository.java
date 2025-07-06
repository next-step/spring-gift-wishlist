package gift.auth.repository;

import gift.auth.domain.MemberAuth;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberAuthRepository implements MemberAuthRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert jdbcInsert;

  @Autowired
  public JdbcMemberAuthRepository(DataSource dataSource) {
    this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    this.jdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("member_auth")
        .usingGeneratedKeyColumns("member_id");
  }

  public JdbcMemberAuthRepository(NamedParameterJdbcTemplate jdbcTemplate,
      SimpleJdbcInsert jdbcInsert) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcInsert = jdbcInsert;
  }

  @Override
  public Long save(MemberAuth memberAuth) {
    Objects.requireNonNull(memberAuth,"memberAuth는 null일 수 없습니다.");
    SqlParameterSource params = new BeanPropertySqlParameterSource(memberAuth);
    Number key = jdbcInsert.executeAndReturnKey(params);
    return key.longValue();
  }

  @Override
  public Optional<MemberAuth> findById(Long memberId) {
    Objects.requireNonNull(memberId, "ID은 null이 될 수 없습니다.");
    String sql = "SELECT * FROM member_auth WHERE member_id = :memberId";
    try {
      Map<String, Object> params = Map.of("memberId", memberId);
      return Optional.of(jdbcTemplate.queryForObject(sql, params, memberAuthRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void update(Long memberId, MemberAuth updatedMemberAuth) {
    Objects.requireNonNull(memberId, "ID는 null일 수 없습니다");
    Objects.requireNonNull(updatedMemberAuth, "updatedMember는 null일 수 없습니다");

    String sql =
        "UPDATE member_auth SET member_id = :memberId, email = :email, password = :password, refresh_token = :refreshToken "
            +
            "WHERE member_id = :memberId";

    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("email",updatedMemberAuth.email())
        .addValue("password",updatedMemberAuth.password())
        .addValue("refreshToken",updatedMemberAuth.refreshToken());

    int affected = jdbcTemplate.update(sql, params);
    if (affected == 0) {
      throw new IllegalArgumentException("memberAuth 수정 실패");
    }
  }

  @Override
  public void delete(Long memberId) {
    Objects.requireNonNull(memberId, "ID는 null일 수 없습니다");

    String sql = "DELETE FROM member_auth WHERE member_id = :memberId";
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("memberId", memberId);

    int affected = jdbcTemplate.update(sql, params);
    if (affected == 0) {
      throw new IllegalArgumentException("memberAuth 삭제 실패");
    }
  }

  private RowMapper<MemberAuth> memberAuthRowMapper() {
    return (rs, rowNum) -> MemberAuth.withId(
        rs.getLong("member_id"),
        rs.getString("email"),
        rs.getString("password"),
        rs.getString("refresh_token")
    );
  }

}
