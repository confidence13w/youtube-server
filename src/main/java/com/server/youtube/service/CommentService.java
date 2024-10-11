package com.server.youtube.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.youtube.domain.Comment;
import com.server.youtube.domain.QComment;
import com.server.youtube.repo.CommentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDAO dao;

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QComment qComment = QComment.comment;

    // 댓글 추가
    public Comment create(Comment vo) {
        return dao.save(vo);
    }

    // 비디오별 상위 댓글들 보여주기 -> SQL문 짜보기!
    /*
     *  SELECT * FROM comment
     *  WHERE video_code = 1
     *    AND parent_code = 0
     *  ORDER BY comment_date DESC
     * */
    public List<Comment> getTopComments(int videoCode) {
        return queryFactory
                .selectFrom(qComment)
                .where(qComment.videoCode.eq(videoCode))
                .where(qComment.parentCode.eq(0))
                .orderBy(qComment.commentDate.desc())
                .fetch();
    }

    // 각 댓글의 하위 댓글들 가져오기
    /*
     *  SELECT * FROM comment
     * WHERE parent_code = 1
     * ORDER BY comment_date ASC
     * */
    public List<Comment> getReComments(int parentCode) {
        return queryFactory.selectFrom(qComment)
                .where(qComment.parentCode.eq(parentCode))
                .orderBy(qComment.commentDate.asc())
                .fetch();
    }

    // 댓글 수정
    public void update(Comment vo) {
        Comment comment = dao.findById(vo.getCommentCode()).get();
        comment.setCommentText(vo.getCommentText());
        dao.save(comment);
    }

    // 댓글 삭제
    public void remove(int commentCode) {
        dao.deleteById(commentCode);
    }
}