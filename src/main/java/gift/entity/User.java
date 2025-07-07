package gift.entity;

public record User(

    String email,

    String password
) {

    public User {
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("이메일 형식에 맞게 입력해야 합니다");
        }
    }

}
