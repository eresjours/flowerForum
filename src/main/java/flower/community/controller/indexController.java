package flower.community.controller;

import flower.community.Datatransfermodel.PaginationDTO;
import flower.community.model.Question;
import flower.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class indexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search)
    {

        // 获取所有话题
        PaginationDTO pagination = questionService.list(search, page, size);
        // 获取热点话题
        List<Question> topQuestions = questionService.getTopList();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("topQuestions", topQuestions);
        return "index";
    }
}
