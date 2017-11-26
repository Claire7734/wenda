package com.project.dao;

import com.project.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Claire on 11/26/17.
 */
@Mapper
public interface UserDao {

    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name, password, salt, head_url";
    String SELECT_FIELDS = "user_id, name, password, salt, head_url";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId}"})
    User selectById(int userId);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, " set password=#{password} where user_id=#{userId}"})
    int updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where user_id=#{userId}"})
    int deleteById(int userId);
}