package com.server.youtube.controller;

import com.server.youtube.domain.Subscribe;
import com.server.youtube.service.SubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/*")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class SubscribeController {

    @Autowired
    private SubscribeService service;

    // 로그인한 사람 - 구독 추가
    @PostMapping("/private/sub")
    public ResponseEntity create(@RequestBody Subscribe vo) {
        service.create(vo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그인한 사람 - 구독 취소
    @DeleteMapping("/private/sub/{subCode}")
    public ResponseEntity remove(@PathVariable(name="subCode") int subCode) {
        service.remove(subCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 해당 채널의 구독자수 가져오기
    @GetMapping("/sub/{channelCode}/count")
    public ResponseEntity count(@PathVariable(name="channelCode") int channelCode) {
        return ResponseEntity.ok(service.count(channelCode));
    }

    // 로그인한 사람의 해당 채널의 구독 체크 여부
    @GetMapping("/private/sub/{channelCode}")
    public ResponseEntity check(@PathVariable(name="channelCode") int channelCode) {
        return ResponseEntity.ok(service.check(channelCode));
    }
}