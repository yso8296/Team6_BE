package supernova.whokie.user.service.dto;

import lombok.Builder;
import supernova.whokie.user.Gender;
import supernova.whokie.user.Role;
import supernova.whokie.user.Users;

import java.time.LocalDate;

public class UserModel {

    @Builder
    public record PickedInfo(
            Long userId,
            String name,
            String imageUrl
    ) {
        public static PickedInfo from(Users user, String imageUrl) {
            return PickedInfo.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .imageUrl(imageUrl)
                    .build();
        }

    }

    @Builder
    public record Info(
            String email,
            Gender gender,
            int age,
            String name,
            Role role,
            String imageUrl,
            LocalDate createdAt
    ) {

        public static UserModel.Info from(Users user, String url) {
            return UserModel.Info.builder()
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .age(user.getAge())
                    .name(user.getName())
                    .role(user.getRole())
                    .imageUrl(url)
                    .createdAt(user.getCreatedAt().toLocalDate())
                    .build();
        }
    }

    @Builder
    public record Point(
            int amount
    ) {

        public static UserModel.Point from(Users user) {
            return UserModel.Point.builder()
                    .amount(user.getPoint())
                    .build();
        }
    }

    @Builder
    public record Login(
            String jwt,
            Long userId
    ) {
        public static UserModel.Login from(String jwt, Long userId) {
            return UserModel.Login.builder()
                    .jwt(jwt)
                    .userId(userId)
                    .build();
        }
    }
}
