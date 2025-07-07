package gift.product.repository;


import gift.product.entity.User;

import java.util.Optional;


public interface UserRepository {

	// 유저 생성
	Long save(User user);

	// 유저 조회 by id
	Optional<User> findById(Long id);

	// 유저 조회 by email
	Optional<User> findByEmail(String email);

	// 유저 조회 by nickname
	Optional<User> findByNickname(String nickName);

	// 유저 이메일 + 비밀번호 존재여부
	Optional<User> findByLoginRequest(String email, String password);
}
