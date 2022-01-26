package com.example.zone.controller;

import com.example.zone.entity.Comment;
import com.example.zone.entity.DiscussPost;
import com.example.zone.entity.Event;
import com.example.zone.event.EventProducer;
import com.example.zone.service.CommentService;
import com.example.zone.service.DiscussPostService;
import com.example.zone.utils.HostHolder;
import com.example.zone.utils.ZoneConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements  ZoneConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private  DiscussPostService discussPostService;

    @Autowired
    private EventProducer eventProducer;



    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());//当前登录用户
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件 setData("postId",discussPostId);
        Event event=new Event().setTopic(TOPIC_COMMENT) //主题
                .setUserId(hostHolder.getUser().getId()) //收到通知的userId
                .setEntityId(comment.getEntityId())     // 收到comment的id
                .setEntityType(comment.getEntityType())//收到通知
                .setData("postId",discussPostId);   //在帖子下收到评论
        if(comment.getEntityType()==ENTITY_TYPE_POST) {//在帖子下收到评论
            DiscussPost target= discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if(comment.getEntityType()==ENTITY_TYPE_COMMENT)//在评论下收到回复
        {
            Comment target=commentService.findCommentById(comment.getEntityId());//评论的实体id
            event.setEntityUserId(target.getUserId());
        }

        eventProducer.fireEvent(event);


        //对





        return "redirect:/discuss/detail/" + discussPostId;
    }

}
