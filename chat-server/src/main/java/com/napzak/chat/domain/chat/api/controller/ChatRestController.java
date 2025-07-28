package com.napzak.chat.domain.chat.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.chat.domain.chat.api.ChatPushFacade;
import com.napzak.chat.domain.chat.api.ChatStoreFacade;
import com.napzak.chat.domain.chat.api.dto.request.ChatRoomCreateRequest;
import com.napzak.chat.domain.chat.api.dto.request.ChatRoomProductIdUpdateRequest;
import com.napzak.chat.domain.chat.api.dto.request.cursor.ChatMessageCursor;
import com.napzak.chat.domain.chat.api.dto.response.ChatMessageListResponse;
import com.napzak.chat.domain.chat.api.dto.response.ChatRoomCreateResponse;
import com.napzak.chat.domain.chat.api.dto.response.ChatRoomEnterResponse;
import com.napzak.chat.domain.chat.api.dto.response.ChatRoomIdListResponse;
import com.napzak.chat.domain.chat.api.dto.response.ChatRoomListResponse;
import com.napzak.chat.domain.chat.api.dto.response.ChatRoomProductIdUpdateResponse;
import com.napzak.chat.domain.chat.api.dto.response.ChatRoomSummary;
import com.napzak.chat.domain.chat.api.code.ChatSuccessCode;
import com.napzak.chat.domain.chat.api.service.ChatMessagePagination;
import com.napzak.chat.domain.chat.api.service.ChatRestService;
import com.napzak.chat.domain.chat.api.service.ChatWebSocketService;
import com.napzak.common.auth.annotation.AuthorizedRole;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.domain.chat.entity.enums.ChatMessageSortOption;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.chat.vo.ChatParticipant;
import com.napzak.domain.store.vo.Store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/chat/rooms")
@RequiredArgsConstructor
public class ChatRestController {

	private final ChatRestService chatRestService;
	private final ChatWebSocketService chatWebSocketService;
	private final ChatStoreFacade chatStoreFacade;
	private final ChatPushFacade chatPushFacade;

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PostMapping
	public ResponseEntity<SuccessResponse<ChatRoomCreateResponse>> createChatRoom(
		@RequestBody ChatRoomCreateRequest request,
		@CurrentMember Long senderId
	){
		Long roomId = chatRestService.createChatRoom(senderId, request.receiverId(), request.productId());
		return ResponseEntity.ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_CREATE_SUCCESS, ChatRoomCreateResponse.of(roomId)));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/ids")
	public ResponseEntity<SuccessResponse<ChatRoomIdListResponse>> getChatRoomIds(
		@CurrentMember Long storeId
	) {
		List<ChatParticipant> myRooms = chatRestService.findMyChatRooms(storeId);
		return ResponseEntity.ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_LIST_RETRIEVE_SUCCESS,
			ChatRoomIdListResponse.of(myRooms.stream().map(ChatParticipant::getRoomId).distinct().toList())));
	}

  @AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping
	public ResponseEntity<SuccessResponse<ChatRoomListResponse>> getChatRooms(
		@RequestParam(required = false) String deviceToken,
		@CurrentMember Long storeId
	) {
		Boolean isMessageAllowed = null;
		if (deviceToken != null) {
			isMessageAllowed = chatPushFacade.findAllowMessageByStoreIdAndDeviceToken(storeId, deviceToken);
		}

		List<ChatParticipant> myRooms = chatRestService.findMyChatRooms(storeId);
		List<Long> roomIds = myRooms.stream().map(ChatParticipant::getRoomId).distinct().toList();

		List<ChatParticipant> opponentParticipants = chatRestService.findOpponentsByRoomIds(roomIds, storeId);
		List<Long> opponentIds = opponentParticipants.stream().map(ChatParticipant::getStoreId).distinct().toList();

		Map<Long, ChatMessage> lastMessages = chatRestService.findLastMessages(roomIds);
		Map<Long, Long> unreadCounts = chatRestService.findUnreadCounts(myRooms, storeId);
		List<Store> opponents = chatStoreFacade.findStoresByStoreIds(opponentIds);

		Map<Long, ChatParticipant> opponentMap = opponentParticipants.stream()
			.collect(Collectors.toMap(ChatParticipant::getRoomId, p -> p));
		Map<Long, Store> opponentStoreMap = opponents.stream()
			.collect(Collectors.toMap(Store::getId, s -> s));

		List<ChatRoomSummary> summaries = myRooms.stream()
			.map(myRoom -> {
				ChatParticipant opponentPart = opponentMap.get(myRoom.getRoomId());
				Store opponent = opponentStoreMap.get(opponentPart.getStoreId());
				return ChatRoomSummary.from(myRoom, opponent, lastMessages, unreadCounts);
			}).toList();

		return ResponseEntity.ok(
			SuccessResponse.of(ChatSuccessCode.CHATROOM_LIST_RETRIEVE_SUCCESS,
				ChatRoomListResponse.of(summaries, isMessageAllowed))
		);
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/{roomId}/messages")
	public ResponseEntity<SuccessResponse<ChatMessageListResponse>> getMessages(
		@PathVariable Long roomId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "100") int size,
		@CurrentMember Long storeId
	) {
		ChatMessageSortOption sortOption = ChatMessageSortOption.OLDEST;
		ChatMessagePagination pagination = chatRestService.findChatRoomMessages(
			roomId, parseCursorValues(cursor, sortOption), size);
		Long opponentLastReadId = chatRestService.findOpponentLastReadMessageId(roomId, storeId);

		ChatMessageListResponse response = ChatMessageListResponse.from(
			pagination, sortOption, storeId, opponentLastReadId
		);

		return ResponseEntity.ok(
			SuccessResponse.of(ChatSuccessCode.MESSAGE_LIST_RETRIEVE_SUCCESS, response)
		);
	}

	@PatchMapping("/{roomId}/product-id")
	public ResponseEntity<SuccessResponse<ChatRoomProductIdUpdateResponse>> updateProductId(
		@PathVariable Long roomId,
		@CurrentMember Long storeId,
		@RequestBody ChatRoomProductIdUpdateRequest request
	){
		Long updatedProductId = chatRestService.updateProductId(roomId, request.newProductId());
		return ResponseEntity.ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_PRODUCT_UPDATE_SUCCESS,
				ChatRoomProductIdUpdateResponse.of(updatedProductId)));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PatchMapping("/{roomId}/enter")
	public ResponseEntity<SuccessResponse<ChatRoomEnterResponse>> enterChatRoom(
		@PathVariable Long roomId,
		@CurrentMember Long storeId
	){
		chatRestService.enterChatRoom(roomId, storeId);
		Long productId = chatRestService.findProductIdByRoomId(roomId);
		return ResponseEntity.ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_ENTER_SUCCESS, ChatRoomEnterResponse.of(productId)));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PatchMapping("/{roomId}/leave")
	public ResponseEntity<SuccessResponse<Void>> leaveChatRoom(
		@PathVariable Long roomId,
		@CurrentMember Long storeId
	) {
		chatRestService.leaveChatRoom(roomId, storeId);
		return ResponseEntity.ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_LEAVE_SUCCESS));
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@PatchMapping("/{roomId}/exit")
	public ResponseEntity<SuccessResponse<Void>> exitChatRoom(
		@PathVariable Long roomId,
		@CurrentMember Long storeId
	) {
		chatRestService.exitChatRoom(roomId, storeId);
		chatWebSocketService.sendSystemMessage(roomId, SystemMessageType.EXIT);
		return ResponseEntity.ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_EXIT_SUCCESS));
	}

	private Long parseCursorValues(String cursor, ChatMessageSortOption chatSortOption) {
		if (cursor == null || cursor.isBlank()) {
			return null;
		}
		if (Objects.requireNonNull(chatSortOption) == ChatMessageSortOption.OLDEST) {
			return ChatMessageCursor.fromString(cursor).getId();
		}
		throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
	}
}
