package com.project.dao;

import com.project.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Claire on 11/26/17.
 */
@Mapper
public interface CommentDao {

    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "user_id, content, created_date, entity_id, entity_type, status";
    String SELECT_FIELDS = "comment_id," + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " form ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType}" +
                    " order by created_date desc LIMIT #{offset},#{limit}"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);


    @Update({"update comment set status=#{status} where comment_id =#commentId"})
    int updateStatus(@Param("commentId") int commentId,
                     @Param("status") int status);
}
