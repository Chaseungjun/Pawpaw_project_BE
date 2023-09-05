package kr.co.pawpaw.api.dto.sms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CheckVerificationCodeRequest {
    @NotBlank
    @Schema(description = "폰 번호")
    private String phoneNumber;
    @NotBlank
    @Schema(description = "인증코드")
    private String code;
}
