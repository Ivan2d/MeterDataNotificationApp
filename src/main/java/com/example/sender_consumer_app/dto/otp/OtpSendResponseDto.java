package com.example.sender_consumer_app.dto.otp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Schema(description = "Otp send response dto")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpSendResponseDto {
    @Schema(description = "email",
            example = "test@test.com")
    private String email;
    @Schema(
            description = "OTP unique identifier for confirmation", example = "e9ec8147-62de-466e-80b0-b34d0cf87ab1"
    )
    @JsonProperty("otp_uid")
    private String uuid;

    @Schema(description = "OTP code", example = "125496")
    @JsonProperty("otp_code")
    private String otpCode;

    @Schema(description = "Otp details")
    @JsonProperty("otp_details")
    private OtpDetailsRequestDto otpDetails;

    @Schema(description = "Otp destination", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("otp_destination")
    private String destination;
}
