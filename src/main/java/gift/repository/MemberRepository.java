package gift.repository;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import java.util.Optional;

public interface MemberRepository {

    public int create(MemberRequestDto requestDto);

    public Optional<Member> findByEmail(String email);

    public int changePassword(Member member, String afterPassword);

    public boolean existsByEmail(String email);

    void resetPassword(Member member);
}
