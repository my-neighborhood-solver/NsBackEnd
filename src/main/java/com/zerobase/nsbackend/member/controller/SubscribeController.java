package com.zerobase.nsbackend.member.controller;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequestMapping("/subscribe")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@RestController
public class SubscribeController {

    private final SubscribeService subscribeService;

    @GetMapping(produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal Member member,
        @RequestHeader(value = "Last-Event-ID",required = false,defaultValue = "") String lastEventId) {
        return subscribeService.subscribe(member.getId(),lastEventId);
    }
}
