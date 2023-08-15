package com.zerobase.nsbackend.member.service;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.Notification;
import com.zerobase.nsbackend.member.dto.NotificationResponse;
import com.zerobase.nsbackend.member.repository.EmitterRepository;
import com.zerobase.nsbackend.member.repository.NotificationRepository;
import com.zerobase.nsbackend.member.type.NotificationType;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = makeTimeIncludeId(memberId);
        Long timeout = 60 * 1000 * 60L;
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));

        //시간 초과된경우 자동으로 레포지토리에서 삭제 처리하는 콜백 등록
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(memberId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    // 데이터가 유실된시점을 파악하기 위해 memberId에 현재시간을 더한다.
    private String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    // 로그인후 sse연결요청시 헤더에 lastEventId가 있다면, 저장된 데이터 캐시에서 id값을 통해 유실된 데이터만 다시 전송
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                .id(eventId)
                .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
            .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
            .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
    @Transactional
    public void send(Member receiver, NotificationType notificationType, String content) {
        Notification notification = notificationRepository.save(Notification.builder()
            .receiver(receiver)
            .content(content)
            .isRead(false)
            .notificationType(notificationType).build());
        notificationRepository.save(notification);
        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        //수신자의 SseEmitter 가져오기
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);

        emitters.forEach(
            (key, emitter) -> {
                //데이터 캐시 저장(유실된 데이터처리하기위함)
                emitterRepository.saveEventCache(key, notification);
                //데이터 전송
                sendNotification(emitter, eventId, key, NotificationResponse.of(notification));
                log.info("notification= {}", NotificationResponse.of(notification).getContent());
            });
    }
}
