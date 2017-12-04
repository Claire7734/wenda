package com.project.service;

import com.project.aspect.LogAspect;
import com.project.dao.QuestionDao;
import com.project.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Claire on 11/26/17.
 */
@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    QuestionDao questionDao;

    public int addQuestion(Question question){
        //TODO 敏感词过滤
        //TODO 添加时question中不包含id，怎么返回？？
        return questionDao.addQuestion(question)>0?question.getQuestionId():0;
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }
}
