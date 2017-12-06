package com.project.service;

import com.project.dao.CommentDao;
import com.project.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Claire on 12/6/17.
 */
@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    CommentDao commentDao;
    @Autowired
    SensitiveWordService sensitiveWordService;

    public List<Comment> getCommentByEntity(int entityId, int entityType, int offset, int limit) {
        return commentDao.selectCommentByEntity(entityId, entityType, offset, limit);
    }

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveWordService.filter(comment.getContent()));
        return commentDao.addComment(comment) > 0 ? comment.getCommentId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId) {
        return commentDao.updateStatus(commentId, 1) > 0;
    }
}
