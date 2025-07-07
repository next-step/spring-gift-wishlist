package gift.service;

import gift.dto.request.ModifyUserRequest;
import gift.dto.request.UserInfoRequest;
import gift.dto.response.UserResponse;
import gift.exception.gift.NoValueException;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static gift.status.UserErrorStatus.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserInfo(UserInfoRequest request) {
        return UserResponse.from(userRepository.findUserByEmail(request.email()).orElseThrow(
                () -> new NoValueException(NO_VALUE.getErrorMessage())
        ));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository
                .getAllUsers()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    public String modifyUserInfo(Long userId, ModifyUserRequest modifyUserRequest){
        userRepository.modify(userId, ModifyUserRequest.toEntity(modifyUserRequest));
        return "유저 "+ userId + "번님을 수정하였습니다.";
    }

    public void deleteById(Long userId){
        userRepository.deleteById(userId);
    }
}
