package com.example.sender_consumer_app.dto.otp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Settings for generating and sending SMS code", requiredMode = Schema.RequiredMode.REQUIRED)
public class OtpDetailsRequestDto {

    @Schema(description = "SMS code length", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("length")
    private Integer length;

    @Schema(description = "Time, storing SMS code (in seconds)", example = "300", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("ttl")
    private Integer ttl;

    @Schema(description = "Number of attempts to enter SMS code", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("attempts")
    private Integer attempts;

    @Schema(description = "Time to resend SMS code (in seconds)", example = "60", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("delay")
    private Integer delay;

    @Schema(description = "Parameter forcing to generate and send a new code", example = "false")
    @JsonProperty("force")
    private Boolean force;
}
