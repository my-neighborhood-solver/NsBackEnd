package chat.web;


import chat.domain.Service.ChattingContentReadStatusService;
import chat.domain.Service.ChattingService;
import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingRoom;
import chat.dto.ChattingContentRequestDTO;
import java.util.Map;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChattingController {

  private final ChattingService chattingService;
  private final ChattingContentReadStatusService chattingContentReadStatusService;

  private final ChannelTopic topic;

  @Resource(name = "chatRedisTemplate")
  private final RedisTemplate redisTemplate;


  @MessageMapping("/messages/{room-id}")
  public void message(@DestinationVariable("room-id") Long roomId,
      ChattingContentRequestDTO chattingContentRequestDTO) {
    ChattingContentRequestDTO dto = ChattingContentRequestDTO
        .fromChattingRoomRequestDTO(chattingContentRequestDTO);
    Member sender = memberService.getmemberById(dto.getSenderId());
    if (sender == null) {
      throw new RuntimeException("Member not found");
    }
    ChattingRoom chattingRoom = chattingService.getChattingRoomById(dto.getRoomId());

    ChattingContent chattingContent = ChattingContent.builder()
        .chattingRoom(chattingRoom)
        .sender(sender)
        .content(dto.getContent())
        .isRead("N")
        .build();

    ChannelTopic topic = ChannelTopic.of(this.topic.getTopic() + chattingRoom.getId());

    redisTemplate.convertAndSend(topic.getTopic(), chattingContent);

    chattingService.saveContent(chattingContent);
  }

  @MessageMapping("/messages/{room-id}/read")
  public void readMessage(
      @DestinationVariable("room-id") Long roomId,
      @Payload Map<String, Long> payload
  ) {
    Long member_Id = payload.get("member_Id");
    chattingContentReadStatusService.markAsRead(roomId, member_Id);

  }

}
