package gift.member.service;

import gift.domain.Role;
import gift.member.dto.*;

import java.util.List;
import java.util.UUID;

public interface MemberService {

    UUID save(MemberCreateDto memberCreateDto);

    void changePassword(String email, MemberUpdateRequest memberUpdateRequest);

    void updateMemberForAdmin(UUID id, MemberUpdateReqForAdmin memberUpdateReqForAdmin);

    MemberResponse findById(UUID id);

    List<MemberResponse> findAll();

    void deleteByEmail(String email);

    void deleteById(UUID id);

    void tokenValidate(String email, String role);

    MemberResponse validate(String email, String password);
}
