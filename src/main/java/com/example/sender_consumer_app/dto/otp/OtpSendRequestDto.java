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
@Schema(description = "Request to generate and send SMS code to the client")
public class OtpSendRequestDto {
    @Schema(description = "email",
            example = "test@test.com")
    private String email;

    @Schema(description = "OTP unique identifier", example = "e9ec8147-62de-466e-80b0-b34d0cf87ab1")
    @JsonProperty("otp_uid")
    private String uuid;

    @Schema(description = "SMS text template", example = "Verification code %s", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("message_template")
    private String messageTemplate;

    @Schema(description = "SMS subject text", example = "Your password changed!", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("message_subject")
    private String subject;

    @Schema(description = "User's uid", example = "e9ec8147-62de-466e-80b0-b34d0cf87ab1")
    @JsonProperty("user_uid")
    private String userUid;

    @Schema(description = "Otp destination", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("destination")
    private String destination;

    @Schema(description = "Settings for generating and sending SMS code", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("otp_details")
    private OtpDetailsRequestDto otpDetails;
}
