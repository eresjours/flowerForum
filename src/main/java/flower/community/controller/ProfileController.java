package flower.community.controller;

import flower.community.Datatransfermodel.PaginationDTO;
import flower.community.mapper.UserMapper;
import flower.community.model.User;
import flower.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    private String profile(HttpServletRequest request,
                           @PathVariable(name = "action") String action,
                           Model model,
                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                           @RequestParam(name = "size", defaultValue = "5") Integer size
    ){

        /*
            在用户访问网站首页时检查是否存在有效的身份认证信息（通过Cookie中的token）
            如果存在则将用户信息存储到会话中，以便后续使用。
         */
        Cookie[] cookies = request.getCookies();
        User user = null;
        //当清除cookie以后，没有任何cookie时会产生空指针异常
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    // 如果user对象不为空说明登录成功
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        // 如果用户未登录，返回登录界面
        if (user == null)
            return "redirect:/";

        if ("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("repies".equals(action)) {
            model.addAttribute("section", "repies");
            model.addAttribute("sectionName", "最新回复");
        }

        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination", paginationDTO);
        return "profile";
    }
}
