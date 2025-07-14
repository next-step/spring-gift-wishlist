package gift.repository;

import gift.dto.TokenResponseDto;
import gift.entity.Member;

public interface MemberRepository {

    void saveMember(String email, String password, String role);

    Integer countMember(String email, String password);

    Member findByEmail(String email);

    void deleteAllMembers();
}
