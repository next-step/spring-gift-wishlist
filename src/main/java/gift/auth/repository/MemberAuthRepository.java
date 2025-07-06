package gift.auth.repository;

import gift.auth.domain.MemberAuth;
import java.util.Optional;

public interface MemberAuthRepository {
  Long save(MemberAuth memberAuth);
  Optional<MemberAuth> findById(Long memberId);
  void update(Long memberId, MemberAuth updatedMemberAuth);
  void delete(Long memberId);

}
