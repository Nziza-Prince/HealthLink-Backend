package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationRepository repo;


    @GetMapping
    public List<Notification> all() { return repo.findAll(); }


    @PostMapping
    public Notification create(@RequestBody Notification n) {
        n.setCreatedAt(LocalDateTime.now());
        n.setReadFlag(false);
        return repo.save(n);
    }


    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Duration.ofMinutes(30).toMillis());
        new Thread(() -> {
            try {
// Initial ping
                emitter.send(SseEmitter.event().name("ping").data("connected"));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }
}