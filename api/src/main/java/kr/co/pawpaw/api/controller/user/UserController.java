package kr.co.pawpaw.api.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.application.user.UserService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.user.UserEmailResponse;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "user")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "유저 정보 가져오기",
        description = "유저 정보 가져오기"
    )
    @GetMapping
    public ResponseEntity<UserResponse> whoAmI(
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(userService.whoAmI(userId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
    })
    @Operation(
        method = "PUT",
        summary = "유저 유저 이미지 생성 또는 업데이트",
        description = "유저 유저 이미지 생성 또는 업데이트"
    )
    @PutMapping("/image")
    public ResponseEntity<Void> updateUserImage(
        @AuthenticatedUserId final UserId userId,
        @RequestParam final MultipartFile file
    ) {
        userService.updateUserImage(userId, file);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "유저 이메일 찾기",
        description = "유저 이메일 찾기"
    )
    @GetMapping("/email")
    public ResponseEntity<UserEmailResponse> getUserEmail(
        @RequestParam final String name,
        @RequestParam final String phoneNumber
    ) {
        return ResponseEntity.ok(userService.getUserEmail(name, phoneNumber));
    }
}
