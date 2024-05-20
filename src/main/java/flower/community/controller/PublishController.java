package flower.community.controller;

import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.model.Question;
import flower.community.model.User;
import flower.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    /**
     * 处理问题编辑界面展示的 GET 请求处理器方法
     *
     * @param id     问题 ID
     * @param model  Spring MVC 模型对象,用于传递数据到视图
     * @return 问题页面视图
     */
    @GetMapping("publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){

        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        return "publish";
    }

    @GetMapping("/publish")
    private String publish() {
        return "publish";
    }

    /**
     * 处理问题发布请求的 POST 请求处理器方法
     *
     * @param title       问题标题
     * @param description 问题描述
     * @param tag         问题标签
     * @param id          问题 ID,用于编辑问题时传递
     * @param request     HTTP 请求对象
     * @param model       Spring MVC 模型对象,用于传递数据到视图
     * @return 根据操作结果重定向到不同页面
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
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

        // 通过从session中获取用户对象来判断用户是否已登录
        User user = (User) request.getSession().getAttribute("user");
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
        question.setId(id);
        questionService.createOrUpdate(question);   //将获取到的值写入数据库
        return "redirect:/";
    }
}
