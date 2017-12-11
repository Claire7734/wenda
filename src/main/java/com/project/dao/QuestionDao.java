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
    String SELECT_FIELDS = "question_id," + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where question_id=#{questionId}"})
    Question selectById(int questionId);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where question_id=#{questionId}"})
    int updateCommentCount(@Param("questionId") int questionId, @Param("commentCount") int commentCount);

}
