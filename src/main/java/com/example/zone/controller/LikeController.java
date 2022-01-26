package com.example.zone.controller;

import com.example.zone.entity.Event;
import com.example.zone.entity.User;
import com.example.zone.event.EventProducer;
import com.example.zone.service.LikeService;
import com.example.zone.service.UserService;
import com.example.zone.utils.HostHolder;
import com.example.zone.utils.ZoneConstant;
import com.example.zone.utils.ZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements ZoneConstant {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;


    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    //    entityUserId从discuss页面传过来
    public String like(int entityType,int entityId,int entityUserId,int postId){
        User user=hostHolder.getUser();


        likeService.like(user.getId(),entityType, entityId,entityUserId);

        //触发点赞事件

        Event event=new Event();
        event.setTopic(TOPIC_LIKE).setUserId(hostHolder.getUser().getId()).setEntityType(entityType).setEntityId(entityId).setEntityUserId(entityUserId).setData("postId",postId);
        eventProducer.fireEvent(event);

        long likeCount=likeService.findEntityLikeCount(entityType, entityId);
        int likeStatus=likeService.findEntityLikeStatus(user.getId(),entityType, entityId);


        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        return ZoneUtil.getJSONString(0,"",map);
    }
}
