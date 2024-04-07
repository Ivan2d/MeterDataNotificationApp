package com.example.sender_consumer_app.dto.otp;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpCheckRequestDto {

    @NotBlank
    @JsonProperty("otp_uid")
    private String otpUid;

    @NotBlank
    @JsonProperty("otp_code")
    private String otpCode;

    @JsonProperty("destination")
    private String destination;
}
