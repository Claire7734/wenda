package com.project.service;

import com.project.aspect.LogAspect;
import com.project.dao.QuestionDao;
import com.project.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Claire on 11/26/17.
 */
@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    QuestionDao questionDao;
    @Autowired
    SensitiveWordService sensitiveWordService;

    public int addQuestion(Question question) {
        //html过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setContent(sensitiveWordService.filter(question.getContent()));
        question.setTitle(sensitiveWordService.filter(question.getTitle()));

        //TODO 添加时question中不包含id，怎么返回？？
        return questionDao.addQuestion(question) > 0 ? question.getQuestionId() : 0;
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }
}
