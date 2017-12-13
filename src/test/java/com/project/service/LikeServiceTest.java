package com.project.service;

import com.project.WendaApplication;
import com.project.model.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by Claire on 12/13/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class LikeServiceTest {

    @Autowired
    LikeService likeService;

    @Test
    public void like() throws Exception {
        likeService.like(10, EntityType.ENTITY_COMMENT, 10);
    }

    @Test
    public void dislike() throws Exception {

    }

}