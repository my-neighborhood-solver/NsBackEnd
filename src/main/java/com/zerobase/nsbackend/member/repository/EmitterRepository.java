package com.zerobase.nsbackend.member.repository;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    //Emitter 저장
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    //이벤트 저장
    void saveEventCache(String emitterId, Object event);
    //회원과 관련된 모든 Emitter 검색
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
    //회원과 관련된 모든 이벤트 검색
    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);
    //Emitter 삭제
    void deleteById(String id);
    //회원과 관련된 모든 Emitter 삭제
    void deleteAllEmitterStartWithId(String memberId);
    //회원과 관련된 모든 이벤트 삭제
    void deleteAllEventCacheStartWithId(String memberId);
}
