package flower.community.controller;

import flower.community.Datatransfermodel.PaginationDTO;
import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.model.User;
import flower.community.service.NotificationService;
import flower.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}") //占位符方式传参
    private String profile(HttpServletRequest request,
                           @PathVariable(name = "action") String action,
                           Model model,
                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                           @RequestParam(name = "size", defaultValue = "5") Integer size
    ){

        User user = (User) request.getSession().getAttribute("user");
        // 如果用户未登录，返回登录界面
        if (user == null)
            return "redirect:/";

        if ("questions".equals(action)){ //访问我的问题的相关操作
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");

            PaginationDTO<QuestionDTO> paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);

        } else if ("replies".equals(action)) {  //访问最新回复的相关操作

            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            Long unreadCount = notificationService.unreadCount(user.getId());

            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("unreadCount", unreadCount);
        }


        return "profile";
    }
}
