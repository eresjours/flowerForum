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

        List<CommentDTO> comments = commentService.listByQuestionId(id);

        QuestionDTO questionDTO = questionService.getById(id);
        questionService.incView(questionDTO.getId());    // 增加阅读数
        model.addAttribute("question", questionDTO);    //加入model中返回前端
        model.addAttribute("comments", comments);
        return "question";
    }
}
