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

    /*
        进入对应问题的处理
     */
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {

        // 拿到所有回复
        List<CommentDTO> comments = commentService.listByQuestionId(id);

        QuestionDTO questionDTO = questionService.getById(id);
        // 拿到相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        questionService.incView(questionDTO.getId());    // 增加阅读数
        model.addAttribute("question", questionDTO);    //加入model中返回前端
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }

    @GetMapping("/star/{id}")
    public String updateStar(@PathVariable(name = "id") Long id) {

        // 取得点赞问题的ID, 进行更新即可
        questionService.updateLikeCount(id);
        // 完成点赞操作后重定向到原页面
        return "redirect:/question/" + id;
    }
}
