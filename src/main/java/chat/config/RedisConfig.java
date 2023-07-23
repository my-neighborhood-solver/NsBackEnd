package chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@RequiredArgsConstructor
public class RedisConfig {

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, Object> chatRedisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> chatRedisTemplate = new RedisTemplate<>();
    chatRedisTemplate.setConnectionFactory(connectionFactory);
    chatRedisTemplate.setKeySerializer(new StringRedisSerializer());
    chatRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
    return chatRedisTemplate;
  }

  // redis pub/sub 메세지를 처리하는 listener 설정
  @Bean
  public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    return container;
  }

  @Bean
  public ChannelTopic channelTopic() {
    return new ChannelTopic("chattingRoom");
  }

}
