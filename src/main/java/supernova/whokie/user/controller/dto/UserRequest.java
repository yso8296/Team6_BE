package supernova.whokie.user.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import supernova.whokie.user.Gender;
import supernova.whokie.user.service.dto.UserCommand;

import java.time.LocalDate;

public class UserRequest {

    @Builder
    public record Info(
            @NotNull
            String name,
            @NotNull
            String gender,
            @NotNull
            LocalDate birthDate
    ) {
        public UserCommand.Info toCommand() {
            return UserCommand.Info.builder()
                    .name(name)
                    .gender(Gender.fromString(gender))
                    .birthDate(birthDate)
                    .build();
        }
    }
}
