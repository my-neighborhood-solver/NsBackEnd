package chat.web;


import chat.domain.Service.ChattingService;
import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingRoom;
import chat.dto.ChattingContentRequestDto;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChattingController {

  private final ChattingService chattingService;

  private final ChannelTopic topic;

  @Resource(name = "chatRedisTemplate")
  private final RedisTemplate redisTemplate;


  @MessageMapping("/chats/messages/{room-id}")
  public void message(@DestinationVariable("room-id") Long roomId, ChattingContentRequestDto dto){

    ChattingContent chattingContent = ChattingContent.builder()
        .chattingRoom(ChattingRoom.builder().id(roomId).build())
        .senderId(dto.getSenderId())
        .content(dto.getContent())
        .isRead("N")
        .build();


    redisTemplate.convertAndSend(topic.getTopic(), chattingContent);

    chattingService.saveContent(chattingContent, roomId);
  }
}
