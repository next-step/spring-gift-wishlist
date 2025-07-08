package gift.domain.member.dto;

public record SignInRequest(String email, String password, String name, String role) {
}
