package gift.product.service;


import gift.product.dto.CreateUserRequest;
import gift.product.entity.User;
import gift.product.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Long register(CreateUserRequest req) {
		// 중복 검증
		if(userRepository.findByEmail(req.email()).isPresent())
			throw new IllegalArgumentException("이미 사용중인 이메일입니다.");

		if(userRepository.findByNickname(req.nickName()).isPresent())
			throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");

		// 유저 추가
		User user = new User(req.email(), req.password(), req.nickName());

		return userRepository.save(user);

	}

}
