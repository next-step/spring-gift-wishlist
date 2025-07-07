package gift.repository;

import gift.dto.*;
import gift.exception.MemberNotFoundException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static RowMapper<MemberResponseDto> memberRowMapper() {
        return (rs, rowNum) -> {
            Long id    = rs.getLong("id");
            String email = rs.getString("email");
            String role  = rs.getString("role");
            return new MemberResponseDto(id, email, role);
        };
    }

    @Override
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
            INSERT INTO member(email, password, role)
            VALUES (:email, :password, :role)
            """;

        jdbcClient.sql(sql)
                .param("email", memberRequestDto.email())
                .param("password", memberRequestDto.password())
                .param("role", memberRequestDto.role())
                .update(keyHolder);

        Long newId = keyHolder.getKey().longValue();
        return new MemberResponseDto(newId, memberRequestDto.email(), memberRequestDto.role());
    }

    @Override
    public PageResult<MemberResponseDto> findAllMembers(PageRequestDto pageRequestDto) {
        int page   = Math.max(pageRequestDto.page(), 0);
        int size   = pageRequestDto.size();
        int offset = page * size;

        String sql = """
            SELECT id, email, role
            FROM member
            ORDER BY id
            LIMIT :limit OFFSET :offset
            """;

        List<MemberResponseDto> content = jdbcClient.sql(sql)
                .param("limit", size)
                .param("offset", offset)
                .query(memberRowMapper())
                .list();

        int total = jdbcClient.sql("SELECT COUNT(*) FROM member")
                .query(Integer.class)
                .single();

        int totalPages = (int) Math.ceil((double) total / size);

        return new PageResult<>(content, page, totalPages, size, total);
    }

    @Override
    public MemberAuthDto findAuthByEmail(String email) {
        String sql = """
            SELECT id, email, password, role
              FROM member
             WHERE email = :email
            """;

        return jdbcClient.sql(sql)
                .param("email", email)
                .query((rs, rn) -> new MemberAuthDto(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                ))
                .optional()
                .orElseThrow(() -> new MemberNotFoundException(email));
    }

    @Override
    public MemberResponseDto findMemberById(Long id) {
        String sql = """
            SELECT id, email, role
            FROM member
            WHERE id = :id
            """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(memberRowMapper())
                .optional()
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public MemberResponseDto findMemberByEmail(String email) {
        String sql = """
            SELECT id, email, role
            FROM member
            WHERE email = :email
            """;

        return jdbcClient.sql(sql)
                .param("email", email)
                .query(memberRowMapper())
                .optional()
                .orElseThrow(() -> new MemberNotFoundException(email));
    }

    @Override
    public MemberResponseDto updateMember(Long id, MemberUpdateDto memberUpdateDto) {
        if (memberUpdateDto.email() == null && memberUpdateDto.password() == null) {
            return findMemberById(id);
        }

        StringBuilder sql = new StringBuilder("UPDATE member SET ");
        boolean first = true;
        if (memberUpdateDto.email() != null) {
            sql.append("email = :email");
            first = false;
        }
        if (memberUpdateDto.password() != null) {
            if (!first) sql.append(", ");
            sql.append("password = :password");
        }
        sql.append(" WHERE id = :id");

        var spec = jdbcClient.sql(sql.toString());
        if (memberUpdateDto.email() != null) {
            spec = spec.param("email", memberUpdateDto.email());
        }
        if (memberUpdateDto.password() != null) {
            spec = spec.param("password", memberUpdateDto.password());
        }
        spec = spec.param("id", id);

        int updated = spec.update();
        if (updated == 0) {
            throw new MemberNotFoundException(id);
        }

        return findMemberById(id);
    }

    @Override
    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = :id";

        int deleted = jdbcClient.sql(sql)
                .param("id", id)
                .update();

        if (deleted == 0) {
            throw new MemberNotFoundException(id);
        }
    }
}
