package gift.product.service;


import gift.product.dto.CreateUserRequest;
import gift.product.dto.LoginRequest;
import gift.product.dto.LoginResponse;
import gift.product.entity.User;
import gift.product.repository.UserRepository;
import gift.product.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;


	public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	public Long register(CreateUserRequest req) {
		if(userRepository.findByEmail(req.email()).isPresent())
			throw new IllegalArgumentException("이미 사용중인 이메일입니다.");

		if(userRepository.findByNickname(req.nickName()).isPresent())
			throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");

		User user = new User(req.email(), req.password(), req.nickName());
		return userRepository.save(user);

	}


	public LoginResponse login(LoginRequest req) {
		Optional<User> user = userRepository.findByLoginRequest(req.email(), req.password());

		if(user.isEmpty())
			throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못됐습니다.");

		User foundUser = user.get();
		String token = jwtUtil.generateToken(foundUser);

		return new LoginResponse(token);
	}

}
