package com.example.zone.controller;

import com.example.zone.entity.User;
import com.example.zone.service.LikeService;
import com.example.zone.utils.HostHolder;
import com.example.zone.utils.ZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId){
        User user=hostHolder.getUser();


        likeService.like(user.getId(),entityType, entityId);
        long likeCount=likeService.findEntityLikeCount(entityType, entityId);
        int likeStatus=likeService.findEntityLikeStatus(user.getId(),entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        return ZoneUtil.getJSONString(0,"",map);
    }
}
