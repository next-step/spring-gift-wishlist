package gift.member.service;

import gift.member.dto.MemberCreateRequest;
import gift.member.dto.MemberResponse;
import gift.member.dto.MemberUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MemberService {

    UUID save(MemberCreateRequest memberCreateRequest);

    void changePassword(String email, MemberUpdateRequest memberUpdateRequest);

    MemberResponse findById(UUID id);

    List<MemberResponse> findAll();

    void deleteMember(String email);

    void tokenValidate(String email, String role);

    MemberResponse validate(String email, String password);
}
