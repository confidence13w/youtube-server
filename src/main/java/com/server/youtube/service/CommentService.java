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

        // 조건 : 자식 댓글이 있는지 체크
        // SELECT count(*) FROM comment WHERE parent_code = :commentCode
        List<Comment> comments = queryFactory.selectFrom(qComment)
                .where(qComment.parentCode.eq(commentCode)).fetch();
        int childCount = comments.size();

        Comment comment = dao.findById(commentCode).get();

        if(childCount > 0) {
            // 자식 댓글이 있는 경우
            // -> 부모 댓글은 삭제되지 않고 "삭제된 댓글입니다" 표시
            // -> isDelete 값 true로 변환
            comment.setDelete(true);
            dao.save(comment);
        } else {
            // 자식 댓글이 없는 경우
            // -> 부모 댓글 DB에서 완전 삭제
            dao.deleteById(commentCode);
        }

        //  해당 댓글의 부모 댓글이 있는지 체크
        if(comment.getParentCode() > 0) {
            // 부모 댓글의 자식 댓글이 모두 삭제되었는지 체크
            List<Comment> parents = queryFactory.selectFrom(qComment)
                    .where(qComment.parentCode.eq(comment.getParentCode())).fetch();
            int parentCount = parents.size(); // 0인 경우
            if(parentCount == 0) {
                // 부모 댓글이 "삭제된 댓글입니다" isDelete가 true인 상태인 경우
                Comment parent = dao.findById(comment.getParentCode()).get();
                if(parent.isDelete()) {
                    dao.deleteById(parent.getCommentCode());
                }
            }
        }
    }
}