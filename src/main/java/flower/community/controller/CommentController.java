package flower.community.controller;

import flower.community.Datatransfermodel.CommentDTO;
import flower.community.Datatransfermodel.ResultDTO;
import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import flower.community.model.Comment;
import flower.community.model.User;
import flower.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /*
        页面提交json格式的数据发送到服务端，存入数据库
     */
    @ResponseBody //将返回的数据自动反序列化为json形式返回前端
    @RequestMapping(value = "/comment", method = RequestMethod.POST) //设置接口访问的url，同时设置只允许post请求
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){ // 将接收到的json反序列化后自动赋值到 commentDTO

        // 通过request获取session，进而获取其中的user
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();    // 返回前端的数据
    }
}