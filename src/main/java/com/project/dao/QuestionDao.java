package com.project.dao;

import com.project.model.Question;
import com.project.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Claire on 11/26/17.
 */
@Mapper
public interface QuestionDao {

    String TABLE_NAME = "question";
    String INSERT_FIELDS = "title, content, created_date, user_id, comment_count";
    String SELECT_FIELDS = "question_id" + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
}
