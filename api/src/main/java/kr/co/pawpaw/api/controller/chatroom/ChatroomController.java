package kr.co.pawpaw.api.controller.chatroom;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.aop.ChatroomRoleCheck;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.chatroom.*;
import kr.co.pawpaw.api.service.chatroom.ChatroomService;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.domainrdb.chatroom.dto.*;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "chatroom")
@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatroomController {
    private final ChatroomService chatroomService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "채팅방 생성 API",
        description = "채팅방 생성 API, 자동으로 채팅방 매니저로 등록됨"
    )
    @PostMapping(consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<CreateChatroomResponse> createChatroom(
        @RequestPart(required = false) final MultipartFile image,
        @RequestPart @Valid final CreateChatroomRequest body,
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(chatroomService.createChatroom(userId, body, image));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @Operation(
        method = "GET",
        summary = "채팅방 기본 커버(이미지) 조회 API",
        description = "채팅방 기본 커버 이미지 id 및 url 조회 API"
    )
    @GetMapping("/default-cover")
    public ResponseEntity<List<ChatroomCoverResponse>> getChatroomDefaultCover() {
        return ResponseEntity.ok(chatroomService.getChatroomDefaultCoverList());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 채팅방 기본 커버입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "채팅방 생성 API(기본 커버 이미지로)",
        description = "채팅방 생성 API(기본 커버 이미지로), 자동으로 채팅방 매니저로 등록됨"
    )
    @PostMapping("/default")
    public ResponseEntity<CreateChatroomResponse> createChatroomWithDefaultCover(
        @RequestBody @Valid final CreateChatroomWithDefaultCoverRequest request,
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(chatroomService.createChatroomWithDefaultCover(userId, request));
    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode= "409",
            description = "이미 채팅방에 참여하였습니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "채팅방 입장 API(참여자)",
        description = "채팅방 입장 API(참여자)"
    )
    @PostMapping("/{chatroomId}/participants")
    public ResponseEntity<Void> joinChatroom(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long chatroomId
    ) {
        chatroomService.joinChatroom(userId, chatroomId);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 매니저는 채팅방을 나갈 수 없습니다.",
            content = @Content
        )
    })
    @Operation(
        method = "DELETE",
        summary = "채팅방 퇴장 API(참여자)",
        description = "채팅방 퇴장 API(참여자)"
    )
    @DeleteMapping("/{chatroomId}/participants")
    public ResponseEntity<Void> leaveChatroom(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long chatroomId
    ) {
        chatroomService.leaveChatroom(userId, chatroomId);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @Operation(
        method = "GET",
        summary = "참여중인 채팅방 목록 조회",
        description = "참여중인 채팅방 목록 조회"
    )
    @GetMapping("/participated")
    public ResponseEntity<List<ChatroomDetailResponse>> getParticipatedChatroomList(
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(chatroomService.getParticipatedChatroomList(userId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @Operation(
        method = "GET",
        summary = "추천하는 채팅방 목록 조회",
        description = "추천하는 채팅방 목록 조회"
    )
    @GetMapping("/recommended")
    public ResponseEntity<List<ChatroomResponse>> getRecommendedNewChatroomList(
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(chatroomService.getRecommendedNewChatroomList(userId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @Operation(
        method = "GET",
        summary = "뜨고있는 채팅방 목록 조회",
        description = "뜨고있는 채팅방 목록 조회"
    )
    @GetMapping("/trending")
    public ResponseEntity<Slice<TrendingChatroomResponse>> getTrendingChatroomList(
        @AuthenticatedUserId final UserId userId,
        @RequestParam(name = "beforeId", required = false) final Long beforeId,
        @RequestParam(name = "size", defaultValue = "12") final int size
    ) {
        return ResponseEntity.ok(chatroomService.getTrendingChatroomList(userId, beforeId, size));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "채팅방 참여자 목록 조회",
        description = "채팅방 참여자 목록 조회"
    )
    @ChatroomRoleCheck(role = ChatroomParticipantRole.PARTICIPANT)
    @GetMapping("/{chatroomId}/participants")
    public ResponseEntity<List<ChatroomParticipantResponse>> getChatroomParticipantList(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long chatroomId
    ) {
        return ResponseEntity.ok(chatroomService.getChatroomParticipantResponseList(chatroomId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한이 부족합니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "채팅방 미참여 유저 검색",
        description = "채팅방 미참여 유저 검색"
    )
    @ChatroomRoleCheck(role = ChatroomParticipantRole.MANAGER)
    @GetMapping("/{chatroomId}/non-participants")
    public ResponseEntity<List<ChatroomNonParticipantResponse>> searchChatroomNonParticipants(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long chatroomId,
        @RequestParam final String nickname
    ) {
        return ResponseEntity.ok(chatroomService.searchChatroomNonParticipants(chatroomId, nickname));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한이 부족합니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode= "409",
            description = "이미 채팅방에 참여하였습니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "채팅방 유저 초대",
        description = "채팅방 유저 초대"
    )
    @ChatroomRoleCheck(role = ChatroomParticipantRole.MANAGER)
    @PostMapping("/{chatroomId}/invite")
    public ResponseEntity<Void> inviteUser(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long chatroomId,
        @Valid @RequestBody final InviteChatroomUserRequest request
    ) {
        chatroomService.inviteUser(chatroomId, request);

        return ResponseEntity.noContent().build();
    }
}
