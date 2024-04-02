package flower.community.controller;

import flower.community.mapper.QuestionMapper;
import flower.community.mapper.UserMapper;
import flower.community.model.Question;
import flower.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    private String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            HttpServletRequest request,
            Model model
            ) {

        /*
            获取前端的相关信息放入 model，为了回显到页面上
         */
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        /*
            进行发布验证
            对 标题、问题描述、标签 进行非空判断
         */
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "问题描述不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        /*
            判断用户是否登录
            通过token拿到用户存在数据库中的user信息
            如果存在就绑定到session上去
         */
        User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null && cookies.length == 0) {
            model.addAttribute("error", "登陆失败，cookie为空");
            return "publish";
        }
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

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        /*
            构建question对象
            创建questionMapper，插入到数据库中
         */
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);    //将获取到的值写入数据库
        return "redirect:/";
    }
}
