package gift.dto;

public record UpdateMemberPasswordRequestDto(
        String email,
        String oldPassword,
        String newPassword
) {

}
