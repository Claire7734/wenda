package com.project.async;

import com.alibaba.fastjson.JSONObject;
import com.project.util.JedisAdapter;
import com.project.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;

/**
 * Created by Claire on 12/14/17.
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
//            BlockingDeque<EventModel> queue = new ArrayBlockingQueue<EventModel>(); TODO 用阻塞队列做
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("事件发送错误" + e.getMessage());
            return false;
        }
    }
}
