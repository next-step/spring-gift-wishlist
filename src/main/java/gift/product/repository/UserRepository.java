package gift.product.repository;


import gift.product.entity.User;

import java.util.Optional;


public interface UserRepository {
	Long save(User user);

	Optional<User> findById(Long id);

	Optional<User> findByEmail(String email);

	Optional<User> findByNickname(String nickName);
}
