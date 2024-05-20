package flower.community.controller;

import flower.community.Datatransfermodel.CommentCreateDTO;
import flower.community.Datatransfermodel.CommentDTO;
import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.service.CommentService;
import flower.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    /**
     * 处理问题详情页面请求的 GET 请求处理器方法
     *
     * @param id     问题 ID
     * @param model  Spring MVC 模型对象,用于传递数据到视图
     * @return 问题详情页面视图
     */
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {

        // 拿到所有回复
        List<CommentDTO> comments = commentService.listByQuestionId(id);
        // 拿到问题
        QuestionDTO questionDTO = questionService.getById(id);
        // 拿到相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        questionService.incView(questionDTO.getId());    // 增加阅读数
        model.addAttribute("question", questionDTO);    //加入model中返回前端
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }

    /**
     * 处理问题点赞请求的 GET 请求处理器方法
     *
     * @param id 问题 ID
     * @return 重定向到问题详情页面
     */
    @GetMapping("/star/{id}")
    public String updateStar(@PathVariable(name = "id") Long id) {

        // 取得点赞问题的ID, 进行更新即可
        questionService.updateLikeCount(id);
        // 完成点赞操作后重定向到原页面
        return "redirect:/question/" + id;
    }

    // todo 可以修改一下
    /**
     * 处理删除问题请求的 GET 请求处理器方法
     *
     * @param id 问题 ID
     * @return 重定向到主页
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id) {

        // 取得要删除问题的ID, 进行删除即可
        questionService.deleteById(id);
        // 完成删除操作后重定向到原页面
        return "redirect:/";
    }
}
