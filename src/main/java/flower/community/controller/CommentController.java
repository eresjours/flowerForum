package flower.community.controller;

import flower.community.Datatransfermodel.CommentCreateDTO;
import flower.community.Datatransfermodel.CommentDTO;
import flower.community.Datatransfermodel.ResultDTO;
import flower.community.exception.CustomizeErrorCode;
import flower.community.model.Comment;
import flower.community.model.User;
import flower.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /*
        评论 不需要返回值
        页面提交json格式的数据发送到服务端，存入数据库
     */
    @ResponseBody //将返回的数据自动反序列化为json形式返回前端
    @RequestMapping(value = "/comment", method = RequestMethod.POST) //设置接口访问的url，同时设置只允许post请求
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){ // 将接收到的json反序列化后自动赋值到 commentCreateDTO

        // 通过request获取session，进而获取其中的user
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        // 调用 commons-lang3 中的 isBlank 方法，进行非空判断
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) { // 说明前端返回的数据为空
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment, user);
        return ResultDTO.okOf();    // 返回前端的数据
    }

    /*
        二级评论 返回list
     */
    @ResponseBody //将返回的数据自动反序列化为json形式返回前端
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET) //设置接口访问的url，同时设置只允许post请求
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByCommentId(id);
        return ResultDTO.okOf(commentDTOS);
    }
}
