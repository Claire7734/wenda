package com.project.service;

import com.project.WendaApplication;
import com.project.model.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Claire on 2/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class FollowServiceTest {

    @Autowired
    FollowService followService;

    @Test
    public void test() {
        followService.follow(13, EntityType.ENTITY_USER, 9);
    }

}