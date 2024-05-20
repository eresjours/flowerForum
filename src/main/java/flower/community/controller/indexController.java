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

    /**
     * 处理根路径 "/" 的 GET 请求,用于渲染首页
     *
     * @param model  用于将数据传递给视图模板
     * @param page   当前页码,默认为 1
     * @param size   每页显示的条目数,默认为 5
     * @param search 搜索关键词,可选
     * @return 渲染后的首页视图模板名称
     */
    @GetMapping("/")
    public String index(Model model,
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
