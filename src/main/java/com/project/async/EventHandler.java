package com.project.async;

import java.util.List;

/**
 * Created by Claire on 12/14/17.
 */
public interface EventHandler {

    //处理
    void doHandle(EventModel model);

    //注册自己
    List<EventType> getSupportEventTypes();
}
