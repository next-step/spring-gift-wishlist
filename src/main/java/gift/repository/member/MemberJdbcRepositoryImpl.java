package gift.repository.member;

import gift.entity.Member;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class MemberJdbcRepositoryImpl implements MemberRepository {

  private final JdbcTemplate jdbcTemplate;

  public MemberJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Member> findAllMembers() {
    String sql = "select * from members";
    return jdbcTemplate.query(sql, memberRowMapper());
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    String sql = "select * from members where email=?";
    try {
      Member result = jdbcTemplate.queryForObject(sql, memberRowMapper(), email);
      return Optional.of(result);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Member> findById(Long id) {
    String sql = "select * from members where id=?";
    try {
      Member result = jdbcTemplate.queryForObject(sql, memberRowMapper(), id);
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

  @Override
  public Optional<Member> updateMember(Long id, Member member) {
    String sql = "update members set email=?,password=? where id=?";
    int update = jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), id);
    if (update == 0) {
      return Optional.empty();
    }
    member.setId(id);
    return Optional.of(member);
  }

  @Override
  public int deleteMember(Long id) {
    String sql = "delete from members where id=?";
    int deletedMember = jdbcTemplate.update(sql, id);
    if (deletedMember < 1) {
      throw new IllegalStateException("no delete members");
    }
    return deletedMember;
  }

  private static RowMapper<Member> memberRowMapper() {
    return new RowMapper<Member>() {
      @Override
      public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
        );
        return member;
      }
    };
  }

}
