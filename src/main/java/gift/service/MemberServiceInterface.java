package gift.service;

import gift.dto.*;

import java.util.List;

public interface MemberServiceInterface {

    boolean isEmailExists(String email);

    MemberResponseDto register(MemberRequestDto requestDto);

    MemberResponseDto login(MemberRequestDto requestDto);


}
