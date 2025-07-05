package gift.repository;

import gift.dto.TokenResponseDto;

public interface MemberRepository {

    void saveMember(String email, String password, String role);

}
