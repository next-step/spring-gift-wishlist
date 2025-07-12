package gift.service;

import gift.dto.request.UserModifyRequest;
import gift.dto.response.UserResponse;
import gift.entity.User;
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

    // 해당 부분에서 DTO 를 반환하는 것이 아니라, Domain 을 그대로 반환하게 됩니다.
    // 저는 DTO 를 Service 레이어에서 처리하는 식으로 사용하였는데,
    // 이런 식으로 코드를 작성하였을 때, 제가 생각하는 것은 Domain 을 그대로 반환하는 Service 를 만들었다는게 문제점이 될 수 있을 것 같다고
    // 생각하였습니다.
    // 작성이유는 ArgumentHandler 에서 Token 을 통해서 User 에 대한 정보를 DB 에서 가져오기 때문입니다.
    // 혹시 멘토님이 보시기에는 이 문제에 대해서 어떻게 생각하시는지 궁금합니다.
    public User getUserInfo(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NoValueException(NO_VALUE.getErrorMessage()
        ));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    public void modifyUserInfo(Long userId, UserModifyRequest userModifyRequest){
        userRepository.modify(userId, userModifyRequest.toEntity());
    }

    public void deleteById(Long userId){
        userRepository.deleteById(userId);
    }
}
