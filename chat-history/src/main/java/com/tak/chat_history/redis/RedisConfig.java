package com.tak.chat_history.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory connectionFactory,
            RedisSubscriber redisSubscriber,
            ChannelTopic chatMessageTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisSubscriber, chatMessageTopic);
        return container;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // ISO-8601
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory cf,
            ObjectMapper objectMapper
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);

        // 타입정보까지 포함하면 다형성/컬렉션에 안전
        RedisSerializer<Object> json = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(json);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(json);
        template.afterPropertiesSet();
        return template;
    }



    @Bean
    public ChannelTopic chatMessageTopic(){
        return new ChannelTopic("chat-message" );
    }

    @Bean
    public ChannelTopic chatTimestampTopic(){
        return new ChannelTopic("chat-timestamp" );
    }
}
