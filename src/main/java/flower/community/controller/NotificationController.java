package flower.community.controller;

import flower.community.Datatransfermodel.NotificationDTO;
import flower.community.enums.NotificationTypeEnum;
import flower.community.model.User;
import flower.community.service.CommentService;
import flower.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/notification/{id}") //占位符方式传参
    private String profile(HttpServletRequest request,
                           @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        // 如果用户未登录，返回首页
        if (user == null)
            return "redirect:/";

        NotificationDTO notificationDTO = notificationService.read(id, user);
        if (NotificationTypeEnum.REPLAY_QUESTION.getType() == notificationDTO.getType()) {

            return "redirect:/question/" + notificationDTO.getOuterid();

        } else if (NotificationTypeEnum.REPLAY_COMMENT.getType() == notificationDTO.getType()) {

            Long questionId = commentService.getQuestionIdByParentId(notificationDTO.getOuterid());
            return "redirect:/question/" + questionId;

        } else {
            return "redirect:/";
        }

    }
}
