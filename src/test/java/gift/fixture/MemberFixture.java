package gift.fixture;

import gift.entity.member.Member;
import gift.entity.member.value.Role;

public final class MemberFixture {

    private MemberFixture() {
    }

    /**
     * ID와 Role이 설정된 새로운 Member 인스턴스를 리턴합니다. email, rawPasswordHash는 그대로 넣어주세요.
     */
    public static Member newRegisteredMember(
            long id, String email, String passwordHash, Role role) {

        // Member.register() 로 생성한 뒤
        Member m = Member.register(email, passwordHash)
                .withRole(role);
        // 테스트 전용으로 id를 세팅할 수 있는 메서드가 없다면,
        // 리플렉션 또는, Member 클래스에 테스트 전용 package-private 생성자를 추가해 두세요.
        // 가령 Member.of(id, email, passwordHash, role, createdAt) 같은 헬퍼가 있으면 더 좋고요.

        // 가정: Member 클래스에 테스트 전용 메서드가 있다면:
        m = m.withId(id);  // 예: package-private setter
        return m;
    }
}
