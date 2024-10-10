package com.server.youtube.controller;

import com.server.youtube.domain.Comment;
import com.server.youtube.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/*")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class CommentController {

    @Autowired
    private CommentService service;

    // 댓글 추가
    @PostMapping("/private/comment")
    public ResponseEntity add(@RequestBody Comment vo) {
        return ResponseEntity.ok(service.create(vo));
    }

    // 비디오 1개에 따른 댓글 전체 조회
    @GetMapping("/video/{videoCode}/comment")
    public ResponseEntity comments(@PathVariable(name="videoCode") int videoCode) {
        List<Comment> comments = service.getTopComments(videoCode);
        return ResponseEntity.ok(comments);
    }
}
