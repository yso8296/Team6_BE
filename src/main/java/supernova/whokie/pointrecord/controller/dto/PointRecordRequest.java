package supernova.whokie.pointrecord.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class PointRecordRequest {

    @Builder
    public record Earn(
            @NotNull @Min(0)
            int point
    ) {

    }
}
