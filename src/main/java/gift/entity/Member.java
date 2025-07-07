package gift.entity;

import gift.dto.MemberResponseDto;
import gift.dto.MemberUpdateRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateRequestDto;

public record Member(Long id, String email, String password, String name, String role){
    public MemberResponseDto toMemberResponseDto(){
        return new MemberResponseDto(this);
    }

    public Member(Long id, MemberUpdateRequestDto requestDto) {
        this(id, requestDto.email(), null, requestDto.name(), requestDto.role());
    }
}
