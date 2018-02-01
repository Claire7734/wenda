import com.project.WendaApplication;
import com.project.model.EntityType;
import com.project.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Claire on 2/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class AddDataTest {
    @Autowired
    FollowService followService;

    @Test
    public void addData() {
        for (int i = 1; i < 5; i++) {
            followService.follow(i, EntityType.ENTITY_USER, 1);
        }
    }
}
