package gift.repository;

import gift.domain.Member;
import gift.domain.Role;
import gift.dto.MemberInfoResponseDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMemberRepository implements MemberRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcTemplateMemberRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public MemberInfoResponseDto saveMember(Member member) {
    String sql = "INSERT INTO members (email, password, role) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, member.getEmail());
      ps.setString(2, member.getPassword());
      ps.setString(3, member.getRole().getRoleName());
      return ps;
    }, keyHolder);

    Long id = keyHolder.getKey().longValue();
    return new MemberInfoResponseDto(id, member.getEmail(), member.getPassword(), member.getRole());
  }

  @Override
  public List<MemberInfoResponseDto> searchAllMembers() {
    String sql = "SELECT * FROM members";
    return jdbcTemplate.query(sql, memberInfoResponseDtoRowMapper());
  }

  @Override
  public Optional<Member> searchMemberById(Long id) {
    String sql = "SELECT * FROM members WHERE id = ?";
    try {
      Member member = jdbcTemplate.queryForObject(sql, memberRowMapper(), id);
      return Optional.ofNullable(member);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Member> searchMemberByEmail(String email) {
    String sql = "SELECT * FROM members WHERE email = ?";
    try {
      Member member = jdbcTemplate.queryForObject(sql, memberRowMapper(), email);
      return Optional.ofNullable(member);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Member updateMember(Long id, String email, String password, Role role) {
    String sql = "UPDATE members SET email = ?, password = ?, role = ? WHERE id = ?";
    String roleName = role.getRoleName();
    jdbcTemplate.update(sql, email, password, roleName, id);
    return searchMemberById(id).get();
  }

  @Override
  public void deleteMember(Long id) {
    String sql = "DELETE FROM members WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  private RowMapper<MemberInfoResponseDto> memberInfoResponseDtoRowMapper() {
    return (rs, rowNum) -> new MemberInfoResponseDto(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password"),
        Role.from(rs.getString("role"))
    );
  }

  private RowMapper<Member> memberRowMapper() {
    return (rs, rowNum) -> new Member(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password"),
        Role.from(rs.getString("role"))
    );
  }
}
