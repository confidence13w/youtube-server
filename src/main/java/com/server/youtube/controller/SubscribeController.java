package com.server.youtube.controller;

import com.server.youtube.domain.Subscribe;
import com.server.youtube.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/*")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class SubscribeController {

    @Autowired
    private SubscribeService service;

    // 구독 추가
    @PostMapping("/sub")
    public ResponseEntity create(@RequestBody Subscribe vo) {
        service.create(vo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 구독 취소
    @DeleteMapping("/sub/{subCode}")
    public ResponseEntity remove(@PathVariable(name="subCode") int subCode) {
        service.remove(subCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 해당 채널의 구독자수 가져오기
    @GetMapping("/sub/{channelCode}/count")
    public ResponseEntity count(@PathVariable(name="channelCode") int channelCode) {
        return ResponseEntity.ok(service.count(channelCode));
    }
}
