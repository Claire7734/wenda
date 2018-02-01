import com.project.WendaApplication;
import com.project.dao.QuestionDao;
import com.project.dao.UserDao;
import com.project.model.EntityType;
import com.project.model.Question;
import com.project.model.User;
import com.project.service.FollowService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by Claire on 11/26/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {

    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    FollowService followService;

    @Test
    public void initDatabase() {
        Random random = new Random();
        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setHeadUrl(String.format("/images/avatar.jpg", i));
            user.setName(String.format("USER-%d", i));
            user.setPassword("xx1");
            user.setSalt("");
            userDao.addUser(user);

            user = userDao.selectByName(user.getName());

            user.setPassword("xx2222");
            int a = userDao.updatePassword(user);

            //相互关注
            for (int j = 1; j < i; j++) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }

            Question question = new Question();
            question.setCommentCount(0);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * i);
            question.setCreatedDate(date);
            question.setUserId(user.getUserId());
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("balabala content %d", i));
            questionDao.addQuestion(question);
            System.out.println(questionDao.selectLatestQuestions(user.getUserId(), 0, 10));
        }


//        Assert.assertEquals("xx1",userDao.selectById(1).getPassword());
//        userDao.deleteById(2);
//        Assert.assertNull(userDao.selectById(2));
    }
}
