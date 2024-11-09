package supernova.whokie.user.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supernova.whokie.global.constants.MessageConstants;
import supernova.whokie.global.exception.EntityNotFoundException;
import supernova.whokie.user.Users;
import supernova.whokie.user.infrastructure.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserReaderService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(
                () -> new EntityNotFoundException(MessageConstants.USER_NOT_FOUND_MESSAGE));

    }

    @Transactional(readOnly = true)
    public boolean isUserExist(Long userId) {
        return userRepository.existsById(userId);
    }

    @Transactional(readOnly = true)
    public List<Users> getUserListByKakaoIdIn(List<Long> kakaoId) {
        return userRepository.findByKakaoIdIn(kakaoId);
    }

    @Transactional(readOnly = true)
    public List<Users> getUserListByUserIdIn(List<Long> userId) {
        return userRepository.findByIdIn(userId);
    }

    @Transactional(readOnly = true)
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<Users> getAllUsersPaging(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
