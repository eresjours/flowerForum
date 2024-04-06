package flower.community.controller;

import flower.community.Datatransfermodel.CommentDTO;
import flower.community.mapper.CommentMapper;
import flower.community.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    /*
        页面提交json格式的数据发送到服务端，存入数据库
     */
    @ResponseBody //将返回的数据自动反序列化为json形式返回前端
    @RequestMapping(value = "/comment", method = RequestMethod.POST) //设置接口访问的url，同时设置只允许post请求
    public Object post(@RequestBody CommentDTO commentDTO){ // 将接收到的json反序列化后自动赋值到 commentDTO
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        System.out.println(commentDTO.getParentId());
        System.out.println(commentDTO.getContent());
        System.out.println(commentDTO.getType());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(1L);
        comment.setLikeCount(0L);
        commentMapper.insert(comment);
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("message", "成功"); // 返回前端的数据
        return null;
    }
}
