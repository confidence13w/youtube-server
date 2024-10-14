package com.server.youtube.controller;

import com.server.youtube.domain.Comment;
import com.server.youtube.domain.CommentDTO;
import com.server.youtube.service.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    // 댓글 수정
    @PutMapping("/private/comment")
    public ResponseEntity update(@RequestBody Comment vo) {
        service.update(vo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 댓글 삭제
    @DeleteMapping("/private/comment/{commentCode}")
    public ResponseEntity remove(@PathVariable(name="commentCode") int commentCode) {
        service.remove(commentCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 비디오 1개에 따른 댓글 전체 조회
    @GetMapping("/video/{videoCode}/comment")
    public ResponseEntity comments(@PathVariable(name="videoCode") int videoCode) {
        List<Comment> comments = service.getTopComments(videoCode);
        List<CommentDTO> response = commentList(comments);
        return ResponseEntity.ok(response);
    }

    public List<CommentDTO> commentList(List<Comment> comments) {
        List<CommentDTO> response = new ArrayList<>();

        for(Comment comment : comments) {
            List<Comment> replies = service.getReComments(comment.getCommentCode());
            List<CommentDTO> repliesDTO = commentList(replies);
            CommentDTO dto = commentDetail(comment);
            dto.setReplies(repliesDTO);
            response.add(dto);
        }

        return response;
    }

    public CommentDTO commentDetail(Comment comment) {
        return CommentDTO.builder()
                .commentCode(comment.getCommentCode())
                .commentText(comment.getCommentText())
                .commentDate(comment.getCommentDate())
                .id(comment.getId())
                .videoCode(comment.getVideoCode())
                .isDelete(comment.isDelete())
                .build();
    }
}